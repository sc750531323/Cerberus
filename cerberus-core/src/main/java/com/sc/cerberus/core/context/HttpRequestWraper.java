package com.sc.cerberus.core.context;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

@Data
public class HttpRequestWraper {
    private FullHttpRequest fullHttpRequest;
    private ChannelHandlerContext channelHandlerContext;
}
