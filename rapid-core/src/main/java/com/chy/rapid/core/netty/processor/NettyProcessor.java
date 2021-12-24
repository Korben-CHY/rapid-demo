package com.chy.rapid.core.netty.processor;

import com.chy.rapid.core.context.HttpRequestWrapper;

/**
 * 处理 Netty 核心逻辑的执行期接口定义
 *
 * @author Korben on 2021/12/23
 */
public interface NettyProcessor {

    /**
     * 核心执行方法
     * @param wrapper
     */
    void process(HttpRequestWrapper wrapper);

    /**
     * 执行器启动方法
     */
    void start();

    /**
     * 执行器关闭、释放资源方法
     */
    void shutdown();
}
