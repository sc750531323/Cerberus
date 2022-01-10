package com.sc.cerberus.core.helper;

import com.sc.cerberus.enums.ResponseCode;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

public class ResponseHelper {
    //获取响应对象
    //todo 自定义的response
    public static FullHttpResponse getHttpResponse(ResponseCode responseCode) {
        String content = "响应错误";
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(content.getBytes()));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.length());
        return httpResponse;
    }
}
