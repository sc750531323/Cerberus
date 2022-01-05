package com.sc.cerberus.core;

/**
 * 生命周期管理接口
 */
public interface LifeCycle {
    //初始化
    void init();


    //开始
    void start();


    //关闭
    void shutdown();
}
