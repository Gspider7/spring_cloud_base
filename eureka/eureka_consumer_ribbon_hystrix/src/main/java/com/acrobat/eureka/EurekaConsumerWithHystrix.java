package com.acrobat.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * eureka服务消费者
 * @author xutao
 * @date 2018-11-12 15:14
 */
@EnableCircuitBreaker                       // 启用服务调用超时失败（熔断），默认超时时间为1秒，避免服务调用阻塞（服务不可用时，还在调用等）

@EnableEurekaClient                         // 注册为客户端，以激活发现服务的能力
@SpringBootApplication
public class EurekaConsumerWithHystrix {

    public static void main(String[] args) {

        SpringApplication.run(EurekaConsumerWithHystrix.class, args);
    }



}
