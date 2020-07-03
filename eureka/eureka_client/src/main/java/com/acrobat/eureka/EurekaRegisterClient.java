package com.acrobat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * eureka服务注册客户端
 * @author xutao
 * @date 2018-11-12 15:14
 */
@EnableEurekaClient                    // 标志这个类是Eureka服务注册客户端
@SpringBootApplication
public class EurekaRegisterClient {

    public static void main(String[] args) {

        SpringApplication.run(EurekaRegisterClient.class, args);
    }
}
