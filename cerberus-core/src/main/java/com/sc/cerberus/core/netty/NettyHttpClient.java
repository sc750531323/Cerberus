package com.sc.cerberus.core.netty;

import com.sc.cerberus.core.helper.AsyncHttpHelper;
import com.sc.cerberus.core.Config;
import com.sc.cerberus.core.LifeCycle;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;

//http client启动类,该类用于转发http请求
public class NettyHttpClient implements LifeCycle {
    private AsyncHttpClient httpClient;
    private DefaultAsyncHttpClientConfig.Builder builder;
    private Config config;
    private EventLoopGroup eventLoopGroupWorker;

    public NettyHttpClient(Config config, EventLoopGroup eventLoopGroupWorker) {
        this.config = config;
        this.eventLoopGroupWorker = eventLoopGroupWorker;
        init();
    }


    public void init() {
        this.builder = new DefaultAsyncHttpClientConfig.Builder()
                .setFollowRedirect(false)
                .setEventLoopGroup(eventLoopGroupWorker)
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                .setMaxRequestRetry(config.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpMaxConnectionsPerHost())
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());

    }

    public void start() {
        this.httpClient = new DefaultAsyncHttpClient(builder.build());
        AsyncHttpHelper.getInstance().init(httpClient);
    }

    public void shutdown() {
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
