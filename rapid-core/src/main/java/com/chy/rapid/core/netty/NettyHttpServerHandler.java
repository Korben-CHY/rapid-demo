package com.chy.rapid.core.netty;

import com.chy.rapid.core.context.HttpRequestWrapper;
import com.chy.rapid.core.netty.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty核心处理Handler
 *
 * @author Korben on 2021/12/23
 */
@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private final NettyProcessor nettyProcessor;

    public NettyHttpServerHandler(NettyProcessor nettyProcessor) {
        this.nettyProcessor = nettyProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper(request, ctx);

            this.nettyProcessor.process(httpRequestWrapper);
        } else {
            log.error("#NettyHttpServerHandler.channelRead# Msg type is not HttpRequest. msg: {}", msg);
            boolean release = ReferenceCountUtil.release(msg);
            if (!release) {
                log.error("#NettyHttpServerHandler.channelRead# Release msg fail. msg: {}", msg);
            }
        }
    }
}
