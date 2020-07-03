package com.acrobat.shiro.controller;

import com.acrobat.shiro.constant.ResponseData;
import com.acrobat.shiro.realm.MyShiroRealm;
import com.acrobat.shiro.utils.JWTUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户接口
 * @author xutao
 * @date 2018-12-11 10:34
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MyShiroRealm myShiroRealm;

    /**
     * 用户鉴权接口
     */
    @RequestMapping("/checkAuth")
    public String checkAuth(HttpServletRequest request, String token, String url) {
        if (token == null || url == null) {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, "没有访问权限", url);
            return JSON.toJSONString(responseData);
        }

        // 解析JWT
        Claims claims = JWTUtil.parseJWT(token);
        if (claims == null) {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, "没有访问权限", url);
            return JSON.toJSONString(responseData);
        }

        // 获取用户信息
        String username = claims.get("username", String.class);
        if (StringUtils.isBlank(username)) {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, "没有访问权限", url);
            return JSON.toJSONString(responseData);
        }
        if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, "token已经失效", url);
            return JSON.toJSONString(responseData);
        }

        // 获取用户权限信息
        PrincipalCollection principalCollection = new SimplePrincipalCollection(username, myShiroRealm.getName());
        AuthorizationInfo authorizationInfo = myShiroRealm.doGetAuthorizationInfo(principalCollection);
        Set<String> roles = new HashSet<>(authorizationInfo.getRoles());
        System.out.println("roles: " + JSON.toJSONString(roles));
        Set<String> permissions = new HashSet<>(authorizationInfo.getStringPermissions());
        System.out.println("permissions: " + JSON.toJSONString(permissions));

        // todo 判断用户有没有权限访问指定url


        if ("/test/a".equals(url)) {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_YES, "有访问权限", url);
            return JSON.toJSONString(responseData);
        }
        else {
            ResponseData responseData = new ResponseData(ResponseData.AUTHORITY_NO, "没有访问权限", url);
            return JSON.toJSONString(responseData);
        }
    }

    @RequestMapping("/hys")
    public String testHys(String testStr) {

        System.out.println("收到测试熔断的请求" + testStr);
        try {
            Thread.currentThread().sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return testStr;
    }
}
