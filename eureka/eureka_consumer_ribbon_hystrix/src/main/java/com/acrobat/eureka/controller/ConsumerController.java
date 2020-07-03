package com.acrobat.eureka.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    /**
     * 关于hystrix熔断：
     *      熔断是发生在客户端的
     *      客户端统计每个服务调用在一段时间内调用的次数和失败的次数，请求次数超过阈值，且失败百分比超过阈值，开启熔断
     *      熔断打开一段时间后，客户端会尝试在调用时调用服务提供者，如果调用成功率满足条件则关闭熔断，否则重新打开熔断
     *      当请求超时，或者失败，或者熔断已经打开的情况，客户端会执行本地的fallback降级服务，返回一个可以处理的结果
     *      之所以执行降级是为了防止雪崩效应
     *      熔断适用于调用链比较长，调用关系复杂的场景
     *      或者在高并发的场景，使部分消费者进入熔断，另一部分消费者依然可用
     *
     * 关于hystrix资源隔离：
     *      hystrix默认使用线程隔离策略，即对每个服务的调用维护一个线程池，这是为了防止对某个服务的调用代码bug影响对其它服务的调用
     *      Netflix公司内部认为线程隔离开销足够小，不会产生重大的成本或性能的影响
     *      还有一种是信号量隔离，不涉及线程池，为每个服务分配一个信号量上限，同时只允许特定数量的线程调用特定服务
     *
     * 雪崩效应：
     *      在微服务架构中通常会有多个服务层调用，基础服务的故障可能会导致级联故障，进而造成整个系统不可用的情况，这种现象被称为服务雪崩效应。
     *      服务雪崩效应是一种因“服务提供者”的不可用导致“服务消费者”的不可用,并将不可用逐渐放大的过程
     * @return
     */
    @GetMapping("/consume")
    @ResponseBody
    // 启用服务降级：服务调用超时返回失败。设置超时时间
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000")
    })
    public String consume() {
        // 注意：使用ribbon实现负载均衡的时候，服务名称不能用下划线，换成中划线
        // 不需要提供端口
        String result = restTemplate.getForObject("http://EUREKA-CLIENT/service/list", String.class);
        return result;
    }

    /**
     * 返回方法和错误信息可以自定义
     */
    public String fallback() {
        return "fallback";
    }

}
