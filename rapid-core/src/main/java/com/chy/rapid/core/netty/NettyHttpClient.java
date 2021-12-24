package com.chy.rapid.core.netty;

import com.chy.rapid.core.LifeCycle;
import com.chy.rapid.core.config.RapidConfig;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClientConfig.Builder;

/**
 * Netty Http 客户端启动类，负责将请求转发给下游的 Http 服务器
 *
 * @author Korben on 2021/12/24
 */
public class NettyHttpClient implements LifeCycle {

    private final RapidConfig rapidConfig;

    private final EventLoopGroup eventLoopGroupWorker;

    private AsyncHttpClient asyncHttpClient;
    public NettyHttpClient(RapidConfig rapidConfig, EventLoopGroup eventLoopGroupWorker) {
        this.rapidConfig = rapidConfig;
        this.eventLoopGroupWorker = eventLoopGroupWorker;
    }

    /**
     * 初始化 AsyncHttpClient
     */
    @Override
    public void init() {
        Builder builder = new Builder()
                .setFollowRedirect(false)
                .setEventLoopGroup(eventLoopGroupWorker)
                .setConnectTimeout(rapidConfig.getHttpConnectTimeout())
                .setRequestTimeout(rapidConfig.getHttpRequestTimeout())
                .setMaxRequestRetry(rapidConfig.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(rapidConfig.getHttpMaxConnections())
                .setMaxConnectionsPerHost(rapidConfig.getHttpConnectionsPerHost())
                .setPooledConnectionIdleTimeout(rapidConfig.getHttpPooledConnectionIdleTimeout());

        DefaultAsyncHttpClientConfig clientConfig = builder.build();
        this.asyncHttpClient = new DefaultAsyncHttpClient(clientConfig);
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
