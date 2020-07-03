package com.acrobat.shiro.controller;

import com.acrobat.shiro.constant.JWTConfig;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 单点登录的验证接口
 * @author xutao
 * @date 2018-12-10 13:53
 */
@Controller
@RequestMapping("/sso")
public class SSOController {

    /**
     * 验证JWT是否有效
     *
     * todo 安全起见，单点登录服务器需要维护业务服务器白名单
     *      每台业务服务器都要维护单点登录服务器的白名单
     */
    @ResponseBody
    @RequestMapping("/verify")
    public String verifyJWT(HttpServletRequest request, HttpServletResponse response,
                            String url) {
        Map<String, Object> resultMap = new HashMap<>();

        String jwt = request.getHeader(JWTConfig.JWT_KEY);
        if (jwt == null || url == null) {
            resultMap.put("error", "invalid request");
            return JSON.toJSONString(resultMap);
        }

        // 解析JWT，校验JWT是否包含用户信息，是否超时失效

        // 如果JWT有效，执行返回
        response.addHeader(JWTConfig.JWT_KEY, jwt);
        resultMap.put("url", url);
        return JSON.toJSONString(resultMap);
    }
}
