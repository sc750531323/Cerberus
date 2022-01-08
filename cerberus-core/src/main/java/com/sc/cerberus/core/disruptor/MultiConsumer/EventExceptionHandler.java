package com.sc.cerberus.core.disruptor.MultiConsumer;

public class EventExceptionHandler implements com.lmax.disruptor.ExceptionHandler<Order> {

    @Override
    public void handleEventException(Throwable throwable, long l, Order order) {

    }

    @Override
    public void handleOnStartException(Throwable throwable) {

    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {

    }
}
