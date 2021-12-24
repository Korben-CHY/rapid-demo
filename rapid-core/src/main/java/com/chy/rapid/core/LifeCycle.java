package com.chy.rapid.core;

/**
 * 生命周期管理接口
 *
 * @author Korben on 2021/12/22
 */
public interface LifeCycle {

    /**
     * 生命周期组件的初始化方法
     */
    void init();

    /**
     * 生命周期组件的启动方法
     */
    void start();

    /**
     * 生命周期组件的关闭方法
     */
    void shutdown();
}
