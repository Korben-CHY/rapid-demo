package com.chy.rapid.core.netty;

import com.chy.rapid.common.util.RemotingHelper;
import com.chy.rapid.common.util.RemotingUtil;
import com.chy.rapid.core.LifeCycle;
import com.chy.rapid.core.config.RapidConfig;
import com.chy.rapid.core.netty.processor.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
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
import lombok.extern.slf4j.Slf4j;

/**
 * 承接所有网络请求的核心类
 *
 * @author Korben on 2021/12/22
 */
@Slf4j
public class NettyHttpServer implements LifeCycle {

    private final RapidConfig rapidConfig;
    private final NettyProcessor nettyProcessor;

    private int port = 8888;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup eventLoopGroupBoss;
    private EventLoopGroup eventLoopGroupWorker;

    public NettyHttpServer(RapidConfig rapidConfig, NettyProcessor nettyProcessor) {
        this.rapidConfig = rapidConfig;
        this.nettyProcessor = nettyProcessor;

        if (rapidConfig.getPort() > 0 && rapidConfig.getPort() < 65535) {
            this.port = rapidConfig.getPort();
        }
    }

    @Override
    public void init() {
        this.serverBootstrap = new ServerBootstrap();

        if (useEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(rapidConfig.getEventLoopGroupBoosNum(),
                    new DefaultThreadFactory("NettyBossEpoll"));
            this.eventLoopGroupWorker = new EpollEventLoopGroup(rapidConfig.getEventLoopGroupWorkerNum(),
                    new DefaultThreadFactory("NettyWorkerEpoll"));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(rapidConfig.getEventLoopGroupBoosNum(),
                    new DefaultThreadFactory("NettyBossNio"));
            this.eventLoopGroupWorker = new NioEventLoopGroup(rapidConfig.getEventLoopGroupWorkerNum(),
                    new DefaultThreadFactory("NettyWorkerNio"));
        }
    }

    private boolean useEpoll() {
        return this.rapidConfig.isUseEpoll()
                && RemotingUtil.isLinuxPlatform()
                && Epoll.isAvailable();
    }

    @Override
    public void start() {
        this.serverBootstrap
                .group(eventLoopGroupBoss, eventLoopGroupWorker)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)   // TCP 端口重绑定
                .option(ChannelOption.SO_KEEPALIVE, false)  // 如果两小时内没有通信，TCP会自动发一个活动探测报文
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 65535)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .localAddress(port)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(rapidConfig.getMaxContentLength()),
                                new HttpServerExpectContinueHandler(),
                                new NettyServerConnectManagerHandler(),
                                new NettyHttpServerHandler(nettyProcessor)
                        );
                    }
                });

        if (rapidConfig.isNettyAllocator()) {
            this.serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }

        try {
            this.serverBootstrap.bind().sync();
            log.info("<========== Rapid Server Started On Port {} ==========>", this.port);
        } catch (Exception e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() fail!", e);
        }

    }

    @Override
    public void shutdown() {
        if (this.eventLoopGroupBoss != null) {
            this.eventLoopGroupBoss.shutdownGracefully();
        }
        if (this.eventLoopGroupWorker != null) {
            this.eventLoopGroupWorker.shutdownGracefully();
        }
        log.info("============ Rapid Server shutdown. ============");
    }

    static class NettyServerConnectManagerHandler extends ChannelDuplexHandler {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelRegistered {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelUnregistered {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelActive {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.info("NETTY SERVER PIPELINE: channelInactive {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                    final String remoteAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.warn("NETTY SERVER PIPLINE: userEventTriggered: IDLE {}", remoteAddr);
                    ctx.channel().close();
                }
            }
            ctx.fireUserEventTriggered(evt);
        }
    }

    public EventLoopGroup getEventLoopGroupWorker() {
        return eventLoopGroupWorker;
    }
}
