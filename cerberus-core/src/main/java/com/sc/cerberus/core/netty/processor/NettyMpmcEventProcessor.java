package com.sc.cerberus.core.netty.processor;

import com.sc.cerberus.core.Config;
import com.sc.cerberus.core.context.HttpRequestWraper;

public class NettyMpmcEventProcessor implements NettyProcessor {
    private Config config;
    private NettyCoreProcessor nettyCoreProcessor;

    public NettyMpmcEventProcessor(Config config, NettyCoreProcessor nettyCoreProcessor) {
        this.config = config;
        this.nettyCoreProcessor = nettyCoreProcessor;
    }

    public void process(HttpRequestWraper httpRequestWraper) {

    }

    public void start() {

    }

    public void shutdown() {

    }
}
