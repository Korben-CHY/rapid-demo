package com.chy.rapid.core.netty.processor;

import com.chy.rapid.core.config.RapidConfig;
import com.chy.rapid.core.context.HttpRequestWrapper;

/**
 * Mpmc的核心实现处理器，最终会调用到 {@link NettyCoreProcessor}
 *
 * @author Korben on 2021/12/24
 */
public class NettyMpmcProcessor implements NettyProcessor {

    private final RapidConfig rapidConfig;
    private final NettyCoreProcessor nettyCoreProcessor;

    public NettyMpmcProcessor(RapidConfig rapidConfig, NettyCoreProcessor nettyCoreProcessor) {
        this.rapidConfig = rapidConfig;
        this.nettyCoreProcessor = nettyCoreProcessor;
    }

    @Override
    public void process(HttpRequestWrapper wrapper) {

    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
