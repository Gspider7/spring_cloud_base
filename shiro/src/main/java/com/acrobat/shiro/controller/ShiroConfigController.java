package com.acrobat.shiro.controller;

import com.acrobat.shiro.service.FilterChainConfigService;
import com.acrobat.shiro.service.ShiroCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xutao
 * @date 2018-11-29 19:08
 */
@RestController
@RequestMapping("/shiro")
public class ShiroConfigController {

    @Autowired
    private FilterChainConfigService filterChainConfigService;
    @Autowired
    private ShiroCacheService shiroCacheService;

    @RequestMapping("/refresh")
    public String refreshFilterChainConfig() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");

        filterChainConfigService.refreshFilterChainConfiguration();

        return "更新完成" + sdf.format(new Date());
    }

    @RequestMapping("/clearCache")
    public String clearCache() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss sss");
        shiroCacheService.clearShiroCache();

        return "缓存清理完成" + sdf.format(new Date());
    }
}
