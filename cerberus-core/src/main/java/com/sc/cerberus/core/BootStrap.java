package com.sc.cerberus.core;

public class BootStrap {
    public static void main(String[] args) {
        //加载网关配置信息
        Config config = ConfigLoader.getInstance().load(args);

        //初始化插件

        //注册服务：拉取注册到到注册中心的服务实例信息，监听动态的修改和删除

        //启动容器
        Container container = new Container(config);
        container.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                container.shutdown();
            }
        }));


    }
}
