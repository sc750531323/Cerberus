package com.sc.cerberus.core.netty;

import com.sc.cerberus.core.context.HttpRequestWraper;
import com.sc.cerberus.core.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;

//todo SimpleChannelInboundHandler 与 ChannelInboundHandlerAdapter 的区别
public class NettyHttpServerHandler extends  ChannelInboundHandlerAdapter {
    private NettyProcessor processor;


    public NettyHttpServerHandler(NettyProcessor processor){
        this.processor = processor;
    }

    //所有的请求都会到这里,worker group会调用此方法来处理传入的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpRequestWraper httpRequestWraper = new HttpRequestWraper();
            httpRequestWraper.setFullHttpRequest(request);
            httpRequestWraper.setChannelHandlerContext(ctx);
            //processor
            processor.process(httpRequestWraper);
        }else{
            //never go this way
            //todo info error
            boolean release = ReferenceCountUtil.release(msg);
            if(!release){
                //todo log resurce release failed
            }
        }
        //fireChannelRead.触发channel read事件,如果使用SimpleChannelInboundHandler，会自动释放资源，这里需要放入队列，没有处理完成之前不能释放
        super.channelRead(ctx, msg);
    }


}
