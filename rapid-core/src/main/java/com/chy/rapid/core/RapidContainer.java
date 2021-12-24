package com.chy.rapid.core;

import com.chy.rapid.common.constants.RapidBufferHelper;
import com.chy.rapid.core.config.RapidConfig;
import com.chy.rapid.core.netty.NettyHttpClient;
import com.chy.rapid.core.netty.NettyHttpServer;
import com.chy.rapid.core.netty.processor.NettyBatchEventProcessor;
import com.chy.rapid.core.netty.processor.NettyCoreProcessor;
import com.chy.rapid.core.netty.processor.NettyMpmcProcessor;
import com.chy.rapid.core.netty.processor.NettyProcessor;
import java.util.HashMap;

/**
 * 主流程的容器
 *
 * @author Korben on 2021/12/24
 */
public class RapidContainer implements LifeCycle {

    private final RapidConfig rapidConfig;

    private NettyProcessor nettyProcessor;

    private NettyHttpServer nettyHttpServer;

    private NettyHttpClient nettyHttpClient;

    public RapidContainer(RapidConfig rapidConfig) {
        this.rapidConfig = rapidConfig;

        this.init();
    }

    @Override
    public void init() {
        NettyCoreProcessor nettyCoreProcessor = new NettyCoreProcessor();

        if (RapidBufferHelper.isFlusher(rapidConfig.getBufferType())) {
            this.nettyProcessor = new NettyBatchEventProcessor(rapidConfig, nettyCoreProcessor);
        } else if (RapidBufferHelper.isMpmc(rapidConfig.getBufferType())) {
            this.nettyProcessor = new NettyMpmcProcessor(rapidConfig, nettyCoreProcessor);
        } else {
            this.nettyProcessor = nettyCoreProcessor;
        }

        this.nettyHttpServer = new NettyHttpServer(rapidConfig, nettyProcessor);

        this.nettyHttpClient = new NettyHttpClient(rapidConfig,
                nettyHttpServer.getEventLoopGroupWorker());
    }

    @Override
    public void start() {
        this.nettyProcessor.start();
        this.nettyHttpClient.start();
        this.nettyHttpServer.start();
    }

    @Override
    public void shutdown() {
        this.nettyProcessor.shutdown();
        this.nettyHttpClient.shutdown();
        this.nettyHttpServer.shutdown();
    }
}
