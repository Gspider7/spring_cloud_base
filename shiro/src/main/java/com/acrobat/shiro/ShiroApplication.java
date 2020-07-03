package com.acrobat.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author xutao
 * @date 2018-11-28 16:20
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.acrobat.shiro.dao")                    // mybatis自动扫描bean，不需要加@Component等注释
public class ShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class, args);
    }
}
