package com.acrobat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * eureka服务消费者
 * @author xutao
 * @date 2018-11-12 15:14
 */
@EnableFeignClients
@EnableCircuitBreaker               // 启用hystrix熔断
@SpringBootApplication
public class EurekaConsumer {

    public static void main(String[] args) {

        SpringApplication.run(EurekaConsumer.class, args);
    }
}
