package com.sc.cerberus.core.disruptor;


import com.lmax.disruptor.EventHandler;
import org.apache.commons.lang3.RandomUtils;

/**
 * 消费者也是一个事件
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long l, boolean b) throws Exception {
//        Thread.sleep(RandomUtils.nextInt(1,100));
        System.err.println("消费者消费："+orderEvent.getValue());
    }
}
