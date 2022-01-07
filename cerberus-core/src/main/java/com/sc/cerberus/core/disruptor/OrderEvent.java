package com.sc.cerberus.core.disruptor;

/**
 * ds处理的数据模型
 */
public class OrderEvent {
    private long value ;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
