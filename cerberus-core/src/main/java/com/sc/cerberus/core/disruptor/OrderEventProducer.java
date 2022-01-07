package com.sc.cerberus.core.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    //这里直接改变值，就不用垃圾回收了
    public void putData(ByteBuffer bb){
        //先获取下一个可用 的序号
        long next = ringBuffer.next();
        try{
            //通过序号获取下标的数据
            OrderEvent orderEvent = ringBuffer.get(next);
            //重新设置内容
            orderEvent.setValue(bb.getLong(0));
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            ringBuffer.publish(next);
        }
    }
}
