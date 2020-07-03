package com.acrobat.eureka.controller;

import com.acrobat.eureka.feign.ShiroClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xutao
 * @date 2018-11-12 17:01
 */
@Controller
public class ConsumerController {

    @Autowired
    private ShiroClient shiroClient;



    @GetMapping("/consume")
    @ResponseBody
    public String consume() {
        ThreadLocal<String> localToken = new ThreadLocal<>();

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxLjAiLCJpc3MiOiJJU1NVRVIiLCJzdWIiOiJTVUJKRUNUIiwiYXVkIjoiQVVESUVOQ0UiLCJpYXQiOjE1NDQ1ODE5NDEsIm5iZiI6MTU0NDU4MTk0MSwiZXhwIjoxNTQ0NTg1NTQxLCJ1c2VybmFtZSI6IjExIiwidmFsdWUiOiIxMjM0NTYifQ.hjOmbvo5h_nwMPYlbSPXaSJWYYZeu20umRdq6G5CmF0";
        String result = shiroClient.checkAuth(token, "/test/a");
        return result;
    }

}
