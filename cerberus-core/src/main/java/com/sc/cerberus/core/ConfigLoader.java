package com.sc.cerberus.core;

import com.sc.cerberus.util.PropertiesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 网关配置加载类
 * 网关配置加载规则：
 * 1.运行参数
 * 2.环境变量
 * 3.jvm参数
 * 4.配置文件
 * 5.内部config对象默认值
 * 高的优先级会覆盖掉低优先级的类
 */
public class ConfigLoader {
    private final static ConfigLoader INSTANCE = new ConfigLoader();
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_ENV_PRIFIX = "CERBERUS_";
    private static final String CONFIG_JVM_PRIFIX = "CERBERUS.";

    private Config config = new Config();

    public static  ConfigLoader getInstance(){
        return INSTANCE;
    }

    private ConfigLoader(){}

    public static Config getConfig(){
        return INSTANCE.config;
    }

    public Config load(String args[]){
        //1.配置文件
        InputStream asStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

        if(asStream != null){
            try {
                Properties properties = new Properties();
                properties.load(asStream);
                PropertiesUtils.properties2Object(properties,config);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(asStream != null){
                    try {
                        asStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //2.环境参数
        {
            Properties properties = new Properties();
            Map<String, String> env = System.getenv();
            properties.putAll(env);
            PropertiesUtils.properties2Object(properties,config,CONFIG_ENV_PRIFIX);
        }

        // 3.jvm参数
        {
            Properties properties = System.getProperties();
            PropertiesUtils.properties2Object(properties,config,CONFIG_JVM_PRIFIX);
        }


        // 4.运行参数:--a=b
        if(args != null && args.length != 0){
            Properties properties = new Properties();
            for(String arg : args){
                if(arg.startsWith("--") && arg.contains("=")){
                    properties.put(arg.substring(2,arg.indexOf("=")),arg.indexOf("=") + 1);
                }
            }
        }

        // 5.内部config对象默认值
        return config;
    }

}
