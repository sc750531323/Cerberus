package com.sc.cerberus.core.disruptor;

import com.lmax.disruptor.EventFactory;
//事件工厂类
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
