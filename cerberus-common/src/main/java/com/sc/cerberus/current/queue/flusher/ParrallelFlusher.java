package com.sc.cerberus.current.queue.flusher;

import com.google.common.base.Preconditions;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import  com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParrallelFlusher<E> implements Flusher<E> {
    private RingBuffer<Holder> ringBuffer;
    private EventListener<E> listener;
    private WorkerPool<Holder> workerPool;
    private ExecutorService executorService;
    private EventTranslatorOneArg<Holder,E> eventTranslator;

    public ParrallelFlusher(Builder<E> eBuilder) {
        this.eventTranslator = new HolderEventTranslator();
        this.executorService = Executors.newFixedThreadPool(eBuilder.threads,new ThreadFactoryBuilder().setNameFormat("ParrallelFlusher-"+eBuilder.namePrefix+"-pool-%d").build());
        this.listener = eBuilder.listener;
        //1.创建RingBuffer
        RingBuffer<Holder> ringBuffer = RingBuffer.create(eBuilder.producerType, new HolderEventFactory(), eBuilder.bufferSize, eBuilder.waitStrategy);
        //2.通过RingBuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        //3.创建多个消费者组
        WorkHandler<Holder>[] workHandlers = new WorkHandler[eBuilder.threads];
        for (int i = 0; i < workHandlers.length; i++) {
            workHandlers[i] = new HolderWorkHandler();
        }
        //4.构建多消费者工作池
        this.workerPool = new WorkerPool<Holder>(ringBuffer, sequenceBarrier, new HolderExceptionHandler(), workHandlers);
        //5.设置多个消费者的sequence序号，用于单独统计消费进度，设置到ringBuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
    }

    private static <E> void process(EventListener<E> listener,Throwable e,E event){
        listener.onException(e,-1,event);
    }

    @Override
    public void add(E event) {
        RingBuffer<Holder> tmp = ringBuffer;
        if(tmp == null){
            process(listener,new IllegalStateException("ringbuffer is closed!"),event);
            return;
        }
        try {
            ringBuffer.publishEvent(eventTranslator,event);
        }catch (NullPointerException e){
            process(listener,new IllegalStateException("ringbuffer is closed!"),event);
        }

    }

    @Override
    public void add(E... event) {
        RingBuffer<Holder> tmp = ringBuffer;
        if(tmp == null){
            process(listener,new IllegalStateException("ringbuffer is closed!"),event);
            return;
        }
        try {
            ringBuffer.publishEvents(eventTranslator,event);
        }catch (NullPointerException e){
            process(listener,new IllegalStateException("ringbuffer is closed!"),event);
        }
    }

    private void process(EventListener<E> listener, IllegalStateException e, E[] events) {
        for (E event : events) {
            process(listener,e,event);
        }
    }

    @Override
    public boolean tryAdd(E event) {
        RingBuffer<Holder> tmp = ringBuffer;
        if(tmp == null){
            return false;
        }
        try {
            return  ringBuffer.tryPublishEvent(eventTranslator,event);
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public boolean tryAdd(E... event) {
        RingBuffer<Holder> tmp = ringBuffer;
        if(tmp == null){
            return false;
        }
        try {
            return  ringBuffer.tryPublishEvents(eventTranslator,event);
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public boolean isShutdown() {
        return ringBuffer == null;
    }

    @Override
    public void start() {
        this.ringBuffer = workerPool.start(executorService);
    }

    @Override
    public void shutdown() {
        RingBuffer<Holder> tmp = ringBuffer;
        ringBuffer = null;
        if(tmp == null){
            return;
        }
        if(workerPool != null){
            workerPool.halt();
        }
        if(executorService != null){
            executorService.shutdown();
        }
    }

    public interface EventListener<E> {
        void onEvent(E event) throws Exception;

        void onException(Throwable ex, long sequence, E event);
    }

    public static class Builder<E> {
        private ProducerType producerType = ProducerType.MULTI;
        private int bufferSize = 16 * 1024;
        private int threads = 1;
        private String namePrefix = "";
        private WaitStrategy waitStrategy = new BlockingWaitStrategy();
        private EventListener<E> listener;

        public Builder<E> setProducerType(ProducerType producerType) {
            Preconditions.checkNotNull(producerType);
            this.producerType = producerType;
            return this;
        }

        public Builder<E> setBufferSize(int bufferSize) {
            Preconditions.checkArgument(Integer.bitCount(bufferSize) == 1);
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder<E> setNamePrefix(String namePrefix) {
            Preconditions.checkNotNull(namePrefix);
            this.namePrefix = namePrefix;
            return this;
        }

        public Builder<E> setThreads(int threads) {
            Preconditions.checkArgument(threads > 0);
            this.threads = threads;
            return this;
        }

        public Builder<E> setWaitStrategy(WaitStrategy waitStrategy) {
            Preconditions.checkNotNull(waitStrategy);
            this.waitStrategy = waitStrategy;
            return this;
        }

        public Builder<E> setEventListener(EventListener<E> listener) {
            Preconditions.checkNotNull(listener);
            this.listener = listener;
            return this;
        }

        public ParrallelFlusher<E> build() {
            return new ParrallelFlusher<>(this);
        }
    }

    private class Holder {
        private E event;

        public void setValue(E event) {
            this.event = event;
        }

        @Override
        public String toString() {
            return "holder event = " + event;
        }
    }

    class HolderEventFactory implements EventFactory<Holder> {

        @Override
        public Holder newInstance() {
            return new Holder();
        }
    }

    class HolderWorkHandler implements WorkHandler<Holder> {

        @Override
        public void onEvent(Holder holder) throws Exception {
            listener.onEvent(holder.event);
            holder.setValue(null);
        }
    }

    class HolderExceptionHandler implements ExceptionHandler<Holder> {

        @Override
        public void handleEventException(Throwable throwable, long l, Holder holder) {
            try {
                listener.onException(throwable, l, holder.event);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                holder.setValue(null);
            }
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            throw new UnsupportedOperationException(throwable);
        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            throw new UnsupportedOperationException(throwable);
        }
    }

    private class HolderEventTranslator implements EventTranslatorOneArg<Holder,E>{

        @Override
        public void translateTo(Holder holder, long l, E e) {
            holder.setValue(e);
        }
    }
}
