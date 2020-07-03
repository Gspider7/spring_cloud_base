package com.acrobat.shiro.service;

import com.acrobat.shiro.config.ShiroRedisCacheManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xutao
 * @date 2018-11-30 18:14
 */
@Service
public class ShiroCacheService {

    @Resource(name = "securityManager")
    private DefaultWebSecurityManager securityManager;


    /**
     * 手动清除当前缓存
     */
    public void clearShiroCache() {

        ShiroRedisCacheManager cacheManager = (ShiroRedisCacheManager) securityManager.getCacheManager();
        cacheManager.clear();
    }
}
