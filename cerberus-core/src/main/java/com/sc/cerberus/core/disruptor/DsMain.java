package com.sc.cerberus.core.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

public class DsMain {
    public static void main(String[] args) {
        int ringBufferSize = 1024 * 1024;
        Disruptor<?> disruptor = new Disruptor<Object>(eventFactory, ringBufferSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("ds");
                return thread;
            }
        }, ProducerType.SINGLE, new BlockingWaitStrategy());
    }
}
