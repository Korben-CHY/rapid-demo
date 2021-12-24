package com.chy.rapid.core.context;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Http 请求包装类
 *
 * @author Korben on 2021/12/24
 */
public class HttpRequestWrapper {
    private FullHttpRequest fullHttpRequest;
    private ChannelHandlerContext ctx;

    public HttpRequestWrapper(FullHttpRequest fullHttpRequest,
            ChannelHandlerContext ctx) {
        this.fullHttpRequest = fullHttpRequest;
        this.ctx = ctx;
    }

    public FullHttpRequest getFullHttpRequest() {
        return fullHttpRequest;
    }

    public void setFullHttpRequest(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
