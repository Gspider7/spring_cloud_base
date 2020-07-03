package com.acrobat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * eureka服务消费者
 * @author xutao
 * @date 2018-11-12 15:14
 */
@EnableEurekaClient                         // 注册为客户端，以激活发现服务的能力
@SpringBootApplication
public class EurekaConsumer {

    public static void main(String[] args) {

        SpringApplication.run(EurekaConsumer.class, args);
    }
}
