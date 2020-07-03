package com.acrobat.eureka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xutao
 * @date 2018-11-23 10:27
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @RequestMapping("/webSocket")
    public String toWebSocketTestPage() {

        // 使用thymeleaf时，静态页面放在classpath:templates/下面
        return "webSocket";
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(Integer a, Integer b) {
        if (a != null && b != null) {
            return a + b + "";
        }
        return "invalid input";
    }
}
