package com.chy.rapid.core.config;

import com.chy.rapid.common.util.PropertiesUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * 网关配置信息加载类
 * 网关配置加载规则：高优先级的覆盖低优先级的
 *      运行参数 -> JVM参数 -> 环境变量 -> 配置文件 -> 内部默认属性（优先级最低）
 * @author Korben on 2021/12/22
 */
@Slf4j
public class RapidConfigLoader {

    private static final RapidConfigLoader INSTANCE = new RapidConfigLoader();

    private static final String CONFIG_FILE = "rapid.properties";

    public static RapidConfigLoader getInstance() {
        return INSTANCE;
    }

    public static RapidConfig getRapidConfig() {
        return INSTANCE.rapidConfig;
    }

    private RapidConfig rapidConfig;

    private RapidConfigLoader() {
    }

    public RapidConfig load(String[] args) {
        // 1. 配置文件
        {
            InputStream resource = RapidConfigLoader.class.getResourceAsStream(CONFIG_FILE);
            if (resource != null) {
                Properties properties = new Properties();
                try {
                    properties.load(resource);
                    PropertiesUtils.properties2Object(properties, rapidConfig);
                } catch (IOException e) {
                    log.warn("#RapidConfigLoader# load config from file {} error.", CONFIG_FILE, e);
                } finally {
                    try {
                        resource.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 2. 环境变量
        {
            Map<String, String> systemEnv = System.getenv();
            Properties properties = new Properties();
            properties.putAll(systemEnv);
            PropertiesUtils.properties2Object(properties, rapidConfig);
        }

        // 3. JVM 参数
        {
            Properties properties = System.getProperties();
            PropertiesUtils.properties2Object(properties, rapidConfig);
        }

        // 4. 运行时参数
        {
            if (args != null && args.length > 0) {
                Properties properties = new Properties();
                for (String arg : args) {
                    if (arg.startsWith("--") && arg.contains("=")) {
                        String[] paramPair = arg.substring(2).split("=");
                        if (paramPair.length > 1) {
                            properties.put(paramPair[0], paramPair[1]);
                        }
                    }
                }
                PropertiesUtils.properties2Object(properties, rapidConfig);
            }
        }

        return rapidConfig;
    }
}
