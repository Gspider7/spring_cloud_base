package com.acrobat.shiro.controller;

import com.acrobat.shiro.constant.JWTConfig;
import com.acrobat.shiro.constant.ResponseData;
import com.acrobat.shiro.utils.JWTUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录接口
 * @author xutao
 * @date 2018-11-29 15:55
 */
@RestController
public class LoginController {

    @RequestMapping("/login")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        HttpServletResponse response) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        token.setRememberMe(true);                          // 相当于浏览器的记住我功能，关闭浏览器下次再打开不需要重新登录
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);                       // 执行登录验证，token作为参数传递到realm，验证失败会抛出异常
        } catch (Exception e) {
            return "用户名或密码错误！";
        }

        // 生成JWT令牌并放在header中返回
        String jwt = JWTUtil.createJWT(username);
        response.addHeader(JWTConfig.JWT_KEY, jwt);

        ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_YES, "登录成功", jwt);
        return JSON.toJSONString(responseData);
    }

    @GetMapping("/logout")
    public String logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();

        return "登出成功";
    }


}
