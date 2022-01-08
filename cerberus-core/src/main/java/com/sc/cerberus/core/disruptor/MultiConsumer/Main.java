package com.sc.cerberus.core.disruptor.MultiConsumer;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //1.创建RingBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI, () -> new Order(),1024*1024,new YieldingWaitStrategy());
        //2.通过RingBuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        //3.创建多个消费者组
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < 10; i++) {
            consumers[i] = new Consumer("consumer-"+i);
        }
        //4.构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<Order>(ringBuffer,sequenceBarrier,new EventExceptionHandler(),consumers);
        //5.设置多个消费者的sequence序号，用于单独统计消费进度，设置到ringBuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6.启动workerPoll
        workerPool.start(Executors.newFixedThreadPool(5));

        CountDownLatch latch  = new CountDownLatch(1);
        AtomicInteger c = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            Producer producer = new Producer(ringBuffer);
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int j = 0; j<100; j++) {
                    String uuid = UUID.randomUUID().toString();
                    producer.sendData(uuid);
                    int i1 = c.incrementAndGet();
                    System.out.println("producer data "+ j + ":" + uuid+ ",and count is "+i1);

                }
            }).start();
        }
        Thread.sleep(2000);
        System.err.println("----------线程创建完毕，开始生产数据----------");
        latch.countDown();

        Thread.sleep(10000);

        System.err.println("任务总数:" + consumers[2].getCount());
    }
}
