package com.chy.rapid.core.netty.processor;

import com.chy.rapid.core.context.HttpRequestWrapper;

/**
 * 核心流程的主执行逻辑
 *
 * @author Korben on 2021/12/24
 */
public class NettyCoreProcessor implements NettyProcessor {

    @Override
    public void process(HttpRequestWrapper wrapper) {
        try {
            // 1. 将 FullHttpRequest 转换成我们需要的 ctx

            // 2. 执行所有的过滤器逻辑 FilterChain
        } catch (Throwable t) {
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
