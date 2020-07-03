package com.acrobat.zuul;

import com.acrobat.zuul.filter.MyFilter;
import com.acrobat.zuul.provider.MyFallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author xutao
 * @date 2018-11-16 09:35
 */
@EnableZuulProxy
@EnableCircuitBreaker
@SpringBootApplication
@EnableHystrixDashboard                 // 激活hystrix dashboard监控功能，访问localhost:7010/hystrix进入监控主页
public class ZuulApplication {

    /** 注入过滤器 */
    @Bean
    public MyFilter getMyFilter() {
        return new MyFilter();
    }
//    @Bean
//    public MyRateLimitFilter getMyRateLimitFilter() {
//        return new MyRateLimitFilter();
//    }
    /** 注入熔断处理器，熔断时间=请求时间*重试次数 */
    @Bean
    public MyFallbackProvider getMyFallbackProvider() {
        return new MyFallbackProvider();
    }


    public static void main(String[] args) {

        SpringApplication.run(ZuulApplication.class, args);
    }
}
