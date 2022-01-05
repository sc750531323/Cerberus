package com.sc.cerberus.core;

import com.sc.cerberus.constants.BufferHelper;
import com.sc.cerberus.core.netty.NettyHttpClient;
import com.sc.cerberus.core.netty.NettyHttpServer;
import com.sc.cerberus.core.netty.processor.NettyBatchEventProcessor;
import com.sc.cerberus.core.netty.processor.NettyCoreProcessor;
import com.sc.cerberus.core.netty.processor.NettyMpmcEventProcessor;
import com.sc.cerberus.core.netty.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * 主流程的容器类
 * 让所有的启动的组件一起启动，init，shutdown
 */
@Slf4j
public class Container implements  LifeCycle {
    private Config config;//核心配置类
    private NettyHttpServer httpServer; //接受http请求的server
    private NettyProcessor processor; //核心处理器
    private NettyHttpClient httpClient;

    public Container(Config config){
        this.config = config;
        init();
    }

    public void init() {
        //初始化processor
        NettyCoreProcessor coreProcessor =  new NettyCoreProcessor();

        //是否开启缓冲
        String bufferType = config.getBufferType();
        if(BufferHelper.isFlusher(bufferType)){
            processor = new NettyBatchEventProcessor(config,coreProcessor);
        }else if(BufferHelper.isMpmc(bufferType)){
            processor = new NettyMpmcEventProcessor(config,coreProcessor);
        }else {
            processor = coreProcessor;
        }
        //创建httpServer
        httpServer = new NettyHttpServer(config,processor);
        //创建httpclient
        httpClient = new NettyHttpClient(config,httpServer.getEventLoopGroupWork());

    }

    public void start() {
        processor.start();
        httpServer.start();
        httpClient.start();
        log.info("gateway started...");
    }

    public void shutdown() {
        processor.shutdown();
        httpServer.shutdown();
        httpClient.shutdown();
    }
}
