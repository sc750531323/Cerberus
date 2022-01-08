package com.sc.cerberus.core.disruptor.MultiConsumer;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import com.lmax.disruptor.WorkHandler;
//多消费者与单消费者实现的接口不同
public class Consumer implements WorkHandler<Order>{
    private String consumerId;
    private static AtomicInteger count = new AtomicInteger(0);
    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Order o) throws Exception {
        TimeUnit.MICROSECONDS.sleep(1* random.nextInt(5));
        System.out.println("consumer id is "+consumerId + ",消费的数据id是"+o.getId());
        count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }
}
