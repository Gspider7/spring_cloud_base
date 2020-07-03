package com.acrobat.eureka.feign;

import com.acrobat.eureka.controller.ResponseData;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 以下情况会触发fallback
 *      非HystrixBadRequestException异常
 *      超时
 *      断路器打开
 *      线程池/信号量已满
 */
@Component
@RequestMapping("/fallback/shiro")          // 不能和ShiroClient接口的RequestMapping一样
public class ShiroClientFallback implements ShiroClient {

    @Override
    public String checkAuth(@RequestParam("token") String token, @RequestParam("url") String url) {

        // 如果超时，默认用户有访问权限
        return JSON.toJSONString(ResponseData.getDefault());
    }
}
