package com.acrobat.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 不需要登录认证就可以访问的接口
 * @author xutao
 * @date 2018-12-07 15:47
 */
@Controller
@RequestMapping("/unauthorized")
public class UnauthorizedController {

    @ResponseBody
    @GetMapping("/offline")
    public String kickedOffline() {

        return "您已被踢下线";
    }
}
