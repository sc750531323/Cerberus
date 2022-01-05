package com.sc.cerberus.core;

import com.sc.cerberus.constants.BasicConst;
import com.sc.cerberus.constants.BufferHelper;
import com.sc.cerberus.util.NetUtils;

public class Config {
    private int port = 8888;
    private String id = NetUtils.getLocalIp() + BasicConst.COLON_SEPARATOR + port;
    private String registerAddress = "http://192.168.11.111:2379,http://192.168.11.112:2379,http://192.168.11.113:2379";

    private String namespace = "default";

    private int processThread = Runtime.getRuntime().availableProcessors();

    //netty
    private int eventLoopGroupBossNumber = 1;
    private int eventLoopGroupWorkerNumber = processThread;
    private boolean useEpoll = true;
    private boolean nettyAllocator = true;
    //netty报文缓存大小
    private int maxContentLength = 1024 * 1024 * 64;

    //httpAsync 选项
    private int dubboConnections = processThread;

    // completableFuture回调处理结果 ：whenComplete or whenCompleteAsync
    private boolean whenComplete = true; //单异步模式:default

    //队列模式
    private String bufferType = "";//BufferHelper.FLUSHER;
    private int bufferSize = 1024 * 1024;
    //网关队列等待模式
    private String waitStrategy = "blocking";

    /**http 参数配置*/
    private int httpConnectTimeout = 30000;

    private int httpRequestTimeout = 30 * 1000;

    private int httpMaxRequestRetry = 2;

    private int httpMaxConnections = 10000;

    private int httpMaxConnectionsPerHost = 500;

    private int httpPooledConnectionIdleTimeout = 60 * 1000;


    public int getHttpConnectTimeout() {
        return httpConnectTimeout;
    }

    public void setHttpConnectTimeout(int httpConnectTimeout) {
        this.httpConnectTimeout = httpConnectTimeout;
    }

    public int getHttpRequestTimeout() {
        return httpRequestTimeout;
    }

    public void setHttpRequestTimeout(int httpRequestTimeout) {
        this.httpRequestTimeout = httpRequestTimeout;
    }

    public int getHttpMaxRequestRetry() {
        return httpMaxRequestRetry;
    }

    public void setHttpMaxRequestRetry(int httpMaxRequestRetry) {
        this.httpMaxRequestRetry = httpMaxRequestRetry;
    }

    public int getHttpMaxConnections() {
        return httpMaxConnections;
    }

    public void setHttpMaxConnections(int httpMaxConnections) {
        this.httpMaxConnections = httpMaxConnections;
    }

    public int getHttpMaxConnectionsPerHost() {
        return httpMaxConnectionsPerHost;
    }

    public void setHttpMaxConnectionsPerHost(int httpMaxConnectionsPerHost) {
        this.httpMaxConnectionsPerHost = httpMaxConnectionsPerHost;
    }

    public int getHttpPooledConnectionIdleTimeout() {
        return httpPooledConnectionIdleTimeout;
    }

    public void setHttpPooledConnectionIdleTimeout(int httpPooledConnectionIdleTimeout) {
        this.httpPooledConnectionIdleTimeout = httpPooledConnectionIdleTimeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getProcessThread() {
        return processThread;
    }

    public void setProcessThread(int processThread) {
        this.processThread = processThread;
    }

    public int getEventLoopGroupBossNumber() {
        return eventLoopGroupBossNumber;
    }

    public void setEventLoopGroupBossNumber(int eventLoopGroupBossNumber) {
        this.eventLoopGroupBossNumber = eventLoopGroupBossNumber;
    }

    public int getEventLoopGroupWorkerNumber() {
        return eventLoopGroupWorkerNumber;
    }

    public void setEventLoopGroupWorkerNumber(int eventLoopGroupWorkerNumber) {
        this.eventLoopGroupWorkerNumber = eventLoopGroupWorkerNumber;
    }

    public boolean isUseEpoll() {
        return useEpoll;
    }

    public void setUseEpoll(boolean useEpoll) {
        this.useEpoll = useEpoll;
    }

    public boolean isNettyAllocator() {
        return nettyAllocator;
    }

    public void setNettyAllocator(boolean nettyAllocator) {
        this.nettyAllocator = nettyAllocator;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public int getDubboConnections() {
        return dubboConnections;
    }

    public void setDubboConnections(int dubboConnections) {
        this.dubboConnections = dubboConnections;
    }

    public boolean isWhenComplete() {
        return whenComplete;
    }

    public void setWhenComplete(boolean whenComplete) {
        this.whenComplete = whenComplete;
    }

    public String getBufferType() {
        return bufferType;
    }

    public void setBufferType(String bufferType) {
        this.bufferType = bufferType;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getWaitStrategy() {
        return waitStrategy;
    }

    public void setWaitStrategy(String waitStrategy) {
        this.waitStrategy = waitStrategy;
    }
}
