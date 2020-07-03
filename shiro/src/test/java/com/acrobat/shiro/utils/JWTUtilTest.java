package com.acrobat.shiro.utils;

import io.jsonwebtoken.Claims;
import org.junit.Test;

/**
 * @author xutao
 * @date 2018-12-07 13:37
 */
public class JWTUtilTest {

    @Test
    public void testJWT() {
        String username = "gui";

        // 2次生成的字符串不一样，可能跟时间有关系？
        String jwt1 = JWTUtil.createJWT(username);
        System.out.println(jwt1);
        String jwt2 = JWTUtil.createJWT(username);
        System.out.println(jwt2);

        Claims claims = JWTUtil.parseJWT(jwt1);
        System.out.println(claims.get("username"));
    }
}