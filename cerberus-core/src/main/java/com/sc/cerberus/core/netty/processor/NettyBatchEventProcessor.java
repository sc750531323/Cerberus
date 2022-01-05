package com.sc.cerberus.core.netty.processor;

import com.sc.cerberus.core.Config;
import com.sc.cerberus.core.context.HttpRequestWraper;

/**
 * flusher缓冲队列的核心实现，最终还是会调用coreProcessor
 *
 */
public class NettyBatchEventProcessor implements NettyProcessor {

    private Config config;
    private NettyCoreProcessor coreProcessor;

    public NettyBatchEventProcessor(Config config,NettyCoreProcessor coreProcessor) {
        this.config = config;
        this.coreProcessor = coreProcessor;
    }

    public void process(HttpRequestWraper httpRequestWraper) {

    }

    public void start() {

    }

    public void shutdown() {

    }
}
