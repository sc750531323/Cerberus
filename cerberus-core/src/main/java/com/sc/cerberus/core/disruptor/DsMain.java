package com.sc.cerberus.core.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;


public class DsMain {
    public static void main(String[] args) {
        int ringBufferSize = 1024 * 1024;
        EventFactory<OrderEvent> orderEventFactory = new OrderEventFactory();
        //此处的泛型事件就是disruptor队列里存储的内容
        Disruptor<OrderEvent> disruptor = new Disruptor<>(orderEventFactory, ringBufferSize, r -> {
            Thread thread = new Thread(r);
            thread.setName("ds");
            return thread;
        }, ProducerType.SINGLE, new BlockingWaitStrategy());
        //相当于添加了消费者
        disruptor.handleEventsWith(new OrderEventHandler());
        disruptor.start();

        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        //生产者
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (int i = 0; i < 100; i++) {
            bb.putLong(0,i);
            producer.putData(bb);
        }

        disruptor.shutdown();
    }
}
