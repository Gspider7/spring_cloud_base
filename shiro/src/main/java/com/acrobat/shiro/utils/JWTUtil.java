package com.acrobat.shiro.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

/**
 * JWT(java web token)工具类
 * @author xutao
 * @date 2018-12-07 11:34
 */
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /** JWT私钥，不要泄露 */
    private static final String JWT_PRIVATE_KEY = "acrobat";

    public static Claims parseJWT(String jwt) {
        // 签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 通过私钥生成签名key
        Key signingKey = new SecretKeySpec(Base64.decodeBase64(JWT_PRIVATE_KEY), signatureAlgorithm.getJcaName());

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt.trim()).getBody();
        } catch (Exception e) {
            logger.error("解析JWT失败");
            return null;
        }
        return claims;
    }

    /**
     * 生成token
     */
    public static String createJWT(String username) {
        JwtBuilder jwtBuilder = Jwts.builder();

        long nowMillis = System.currentTimeMillis();
        // 7个官方Payload字段
        jwtBuilder.setId("1.0"); // 编号/版本
        jwtBuilder.setIssuer("ISSUER"); // 发行人（公司）
        jwtBuilder.setSubject("SUBJECT"); // 主题（工程）
        jwtBuilder.setAudience("AUDIENCE"); // 受众
        jwtBuilder.setIssuedAt(new Date(nowMillis)); // 签发时间
        jwtBuilder.setNotBefore(new Date(nowMillis)); // 生效时间
        jwtBuilder.setExpiration(new Date(nowMillis + (60 * 60 * 1000))); // 失效时间

        // 用户自定义字段
        jwtBuilder.claim("username", username);
        jwtBuilder.claim("value", "123456");

        // 签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 通过私钥生成签名key
        Key signingKey = new SecretKeySpec(Base64.decodeBase64(JWT_PRIVATE_KEY), signatureAlgorithm.getJcaName());
        // 进行签名
        jwtBuilder.signWith(signatureAlgorithm, signingKey);

        // 获取token字符串
        String tokenString = jwtBuilder.compact();
        return tokenString;
    }
}
