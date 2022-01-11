package com.sc.cerberus.core.netty.processor;

import com.lmax.disruptor.dsl.ProducerType;
import com.sc.cerberus.core.Config;
import com.sc.cerberus.core.context.HttpRequestWraper;
import com.sc.cerberus.core.helper.ResponseHelper;
import com.sc.cerberus.current.queue.flusher.ParrallelFlusher;
import com.sc.cerberus.enums.ResponseCode;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * flusher缓冲队列的核心实现，最终还是会调用coreProcessor
 *
 */
public class NettyBatchEventProcessor implements NettyProcessor {
private static final String THREAD_NAME_PREFIX = "flusher-";
    private Config config;
    private NettyCoreProcessor coreProcessor;
    private ParrallelFlusher<HttpRequestWraper> parrallelFlusher;

    public NettyBatchEventProcessor(Config config,NettyCoreProcessor coreProcessor) {
        this.config = config;
        this.coreProcessor = coreProcessor;
        ParrallelFlusher.Builder<HttpRequestWraper> builder = new ParrallelFlusher.Builder<HttpRequestWraper>()
                .setBufferSize(config.getBufferSize())
                .setThreads(config.getProcessThread())
                .setProducerType(ProducerType.MULTI)
                .setNamePrefix(THREAD_NAME_PREFIX)
                .setWaitStrategy(config.getATrueStrategy());
        BatchEventProcessorListener listener = new BatchEventProcessorListener();
        builder.setEventListener(listener);
        this.parrallelFlusher = builder.build();
    }

    //监听事件的处理逻辑
    public class BatchEventProcessorListener implements ParrallelFlusher.EventListener<HttpRequestWraper>{

        @Override
        public void onEvent(HttpRequestWraper event) throws Exception {
            coreProcessor.process(event);
        }

        @Override
        public void onException(Throwable ex, long sequence, HttpRequestWraper event) {
            FullHttpRequest fullHttpRequest = event.getFullHttpRequest();
            ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
            try{
                FullHttpResponse httpResponse = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
                // 出错回写
                if(!HttpUtil.isKeepAlive(fullHttpRequest)){
                    channelHandlerContext.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
                }else{
                    httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    channelHandlerContext.writeAndFlush(httpResponse);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void process(HttpRequestWraper httpRequestWraper) {
        //将请求放入环形队列里
        this.parrallelFlusher.add(httpRequestWraper);
    }

    public void start() {
        coreProcessor.start();
        parrallelFlusher.start();
    }

    public void shutdown() {
        coreProcessor.shutdown();
        parrallelFlusher.shutdown();
    }

    public Config getConfig() {
        return config;
    }
}
