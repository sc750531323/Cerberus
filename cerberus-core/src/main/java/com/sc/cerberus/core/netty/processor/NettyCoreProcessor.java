package com.sc.cerberus.core.netty.processor;

import com.sc.cerberus.core.context.HttpRequestWraper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 核心流程主执行逻辑
 */
public class NettyCoreProcessor implements NettyProcessor {
    public void process(HttpRequestWraper event) {
        ChannelHandlerContext context = event.getChannelHandlerContext();
        FullHttpRequest request = event.getFullHttpRequest();
        try{
            //1.解析FullHttpRequest 转化成想要的对象context

            System.out.println("接收到http亲情");


            //2.执行filter chain


            //

        }catch (Throwable t){

        }
    }

    public void start() {

    }

    public void shutdown() {

    }
}
