package com.chy.rapid.core;

import com.chy.rapid.core.config.RapidConfig;
import com.chy.rapid.core.config.RapidConfigLoader;

/**
 * 网关项目启动入口
 *
 * @author Korben on 2021/12/22
 */
public class Bootstrap {

    public static void main(String[] args) {
        // 1. 加载网关配置信息
        RapidConfig rapidConfig = RapidConfigLoader.getInstance().load(args);

        // 2. 初始化插件信息

        // 3. 拉取注册中心服务列表，动态监听服务的变化

        // 4. 启动容器
        RapidContainer rapidContainer = new RapidContainer(rapidConfig);
        rapidContainer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(rapidContainer::shutdown));
    }
}
