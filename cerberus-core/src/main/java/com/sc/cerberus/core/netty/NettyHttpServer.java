package com.sc.cerberus.core.netty;

import com.sc.cerberus.core.Config;
import com.sc.cerberus.core.LifeCycle;
import com.sc.cerberus.core.netty.processor.NettyProcessor;
import com.sc.cerberus.util.RemotingHelper;
import com.sc.cerberus.util.RemotingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultThreadFactory;


import java.net.InetSocketAddress;

/**
 *
 */
public class NettyHttpServer implements LifeCycle {
    private Config config;
    private int port = 8888;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup eventLoopGroupBoss;
    private EventLoopGroup eventLoopGroupWork;
    private NettyProcessor processor;

    public EventLoopGroup getEventLoopGroupWork() {
        return eventLoopGroupWork;
    }

    public NettyHttpServer(Config config, NettyProcessor processor) {
        this.processor = processor;
        this.config = config;
        //判断端口0-65535
        this.port = config.getPort();
        init();
    }

    public void init() {
        this.serverBootstrap = new ServerBootstrap();
        if (useEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(config.getEventLoopGroupBossNumber()
                    , new DefaultThreadFactory("nettyBossEpoll"));
            this.eventLoopGroupWork = new EpollEventLoopGroup(config.getEventLoopGroupWorkerNumber()
                    , new DefaultThreadFactory("nettyWorkerEpoll"));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(config.getEventLoopGroupBossNumber()
                    , new DefaultThreadFactory("nettyBossNio"));
            this.eventLoopGroupWork = new NioEventLoopGroup(config.getEventLoopGroupWorkerNumber()
                    , new DefaultThreadFactory("nettyWorkerNio"));
        }
    }

    /**
     * 判断是否支持epoll模型
     *
     * @return
     */
    public boolean useEpoll() {
        return config.isUseEpoll() && RemotingUtil.isLinuxPlatform() && Epoll.isAvailable();
    }

    public void start() {
        ServerBootstrap serverBootstrap = this.serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWork)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)  //sync + backlog
                .option(ChannelOption.SO_REUSEADDR, true) //端口重绑定
                .option(ChannelOption.SO_KEEPALIVE, false) //2小时没有数据通信的时候，tcp会自动发送一个探测
                .childOption(ChannelOption.TCP_NODELAY, true) //禁止nagle算法，小数据传输合并
                .childOption(ChannelOption.SO_SNDBUF, 65535)  //设置发送缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 65535)  //设置数据缓冲区大小
                .localAddress(new InetSocketAddress(this.port))
                .childHandler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(config.getMaxContentLength()),
                                new HttpServerExpectContinueHandler(),
                                new NettyServerConnectManagerHandler(),
                                new NettyHttpServerHandler(processor)
                        );
                    }
                });
        if (config.isNettyAllocator()) {
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            this.serverBootstrap.bind().sync();
            //todo info log start success
        } catch (InterruptedException e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync()", e);
        }
    }

    public void shutdown() {
        if (eventLoopGroupBoss != null) {
            eventLoopGroupBoss.shutdownGracefully();
        }
        if (eventLoopGroupWork != null) {
            eventLoopGroupWork.shutdownGracefully();
        }
    }

    /**
     * 连接管理器
     */
    static class NettyServerConnectManagerHandler extends ChannelDuplexHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            //todo debug log
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            super.channelInactive(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.ALL_IDLE)) {
                    RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    //todo warn log
                    ctx.channel().close();
                }

            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //todo warn log
            ctx.channel().close();
        }
    }
}
