package com.acrobat.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author xutao
 * @date 2018-11-12 17:01
 */
@Controller
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 通过RestTemplate实现客户端负载均衡
     *
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    @GetMapping("/consume")
    @ResponseBody
    public String consume() {
        // 注意：使用ribbon实现负载均衡的时候，服务名称不能用下划线，换成中划线
        // 不需要提供端口
        String result = restTemplate.getForObject("http://EUREKA-CLIENT/service/list", String.class);
        return result;
    }

}
