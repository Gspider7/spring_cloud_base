package com.acrobat.eureka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xutao
 * @date 2018-11-12 15:47
 */
@RestController
@RequestMapping("/service")
public class RegisterController {


    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 具体服务接口：列出注册中心已注册的所有服务
     */
    @RequestMapping("/list")
    public String listServiceOnRegisterCenter(HttpServletRequest request) throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS :");
        System.out.println(sdf.format(new Date()) + "接收到客户端请求");

//        // 重试测试
//        if (1 == 1) {
//            throw new RuntimeException("发生异常，状态码为500");
//        }

//        // 测试服务降级（超时熔断）
//        Thread.sleep(5000L);

        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }
}
