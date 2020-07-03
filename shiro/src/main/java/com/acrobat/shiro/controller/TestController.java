package com.acrobat.shiro.controller;

import com.acrobat.shiro.constant.ResponseData;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xutao
 * @date 2018-12-11 11:06
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/a")
    public String testA(HttpServletRequest request) {
        String data = request.getHeader("usoft-session-id");

        ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, data, "/test/a 访问成功");
        return JSON.toJSONString(responseData);
    }

    @RequestMapping("/b")
    public String testB(HttpServletRequest request) {
        String data = request.getHeader("usoft-session-id");

        ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, data, "/test/b 访问成功");
        return JSON.toJSONString(responseData);
    }


}
