package com.acrobat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * eureka服务注册中心
 * @author xutao
 * @date 2018-11-12 15:14
 */
@EnableEurekaServer                     // 标志这个类是Eureka服务注册中心
@SpringBootApplication
public class EurekaRegisterCenter {

    public static void main(String[] args) {

        SpringApplication.run(EurekaRegisterCenter.class, args);
    }
}
