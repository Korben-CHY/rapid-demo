package com.chy.rapid.core.config;

import com.chy.rapid.common.constants.BasicConst;
import com.chy.rapid.common.util.NetUtils;
import lombok.Data;

/**
 * 网关的通用配置信息类
 *
 * @author Korben on 2021/12/22
 */
@Data
public class RapidConfig {

    /**
     * 网关的默认端口
     */
    private int port;

    /**
     * 网关服务唯一ID：192.168.1.1:8888
     */
    private String rapidId = NetUtils.getLocalIp() + BasicConst.COLON_SEPARATOR + port;

    /**
     * 网关注册中心地址
     */
    private String registerAddress = "";

    /**
     * 网关的命名空间，dev test prod
     */
    private String nameSpace = "rapid-dev";

    /**
     * 网关服务器CPU核心数映射的线程数
     */
    private int processThread = Runtime.getRuntime().availableProcessors();

    /**
     * Netty的Boss线程数
     */
    private int eventLoopGroupBoosNum = 1;

    /**
     * Netty的Worker线程数
     */
    private int eventLoopGroupWorkerNum = processThread;

    /**
     * 是否开启 EPOLL
     */
    private boolean useEpoll = true;

    /**
     * 是否开启Netty 内存分配机制
     */
    private boolean nettyAllocator = true;

    /**
     * http body报文最大大小
     */
    private int maxContentLength = 64 * 1024 * 1024;

    /**
     * dubbo开启连接数
     */
    private int dubboConnections = processThread;

    /**
     * 设置响应模式, 默认是单异步模式：CompletableFuture回调处理结果： whenComplete  or  whenCompleteAsync
     */
    private boolean whenComplete;

    /**
     * 网关队列配置：缓冲模式；
     *
     * RapidBufferHelper.FLUSHER;
     */
    private String bufferType = "";

    /**
     * 网关队列：内存队列大小
     */
    private int bufferSize = 16 * 1024;

    /**
     * 网关队列：阻塞/等待策略
     */
    private String waitStrategy = "blocking";

    /**
     * 连接超时时间
     */
    private int httpConnectTimeout = 30 * 1000;

    /**
     * 请求超时时间
     */
    private int httpRequestTimeout = 30 * 1000;

    /**
     * 客户端请求重试次数
     */
    private int httpMaxRequestRetry = 2;

    /**
     * 客户端请求最大连接数
     */
    private int httpMaxConnections = 10000;

    /**
     * 客户端每个地址支持的最大连接数
     */
    private int httpConnectionsPerHost = 8000;

    /**
     * 客户端空闲连接超时时间, 默认60秒
     */
    private int httpPooledConnectionIdleTimeout = 60 * 1000;
}
