package com.sc.cerberus.core.netty.processor;

import com.sc.cerberus.core.context.HttpRequestWraper;

public interface NettyProcessor {
    /**
     * 核心执行处理消息的方法
     * @param httpRequestWraper
     */
    public void process(HttpRequestWraper httpRequestWraper);

    /**
     * 执行器启动方法
     */
    public void start();

    /**
     * 执行器关闭资源释放方法
     */
    public void shutdown();
}
