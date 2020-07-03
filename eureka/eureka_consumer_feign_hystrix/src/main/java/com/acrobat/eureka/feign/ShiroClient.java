package com.acrobat.eureka.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于feign的shiro服务消费
 */
//@FeignClient(name = "dataHubAuthManage-service", fallback = ShiroClientFallback.class, configuration = ShiroClientConfig.class)
@FeignClient(name = "SHIRO", fallback = ShiroClientFallback.class)
public interface ShiroClient {

    /**
     * shiro鉴权接口
     */
    @RequestMapping(value = "/auth/checkAuth", produces = "application/json;charset=UTF-8")
    String checkAuth(@RequestParam("token") String token, @RequestParam("url") String url);
}
