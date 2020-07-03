package com.acrobat.shiro.config;

import com.acrobat.shiro.dao.FilterChainConfigDao;
import com.acrobat.shiro.dao.RedisSessionDao;
import com.acrobat.shiro.filter.JWTControlFilter;
import com.acrobat.shiro.filter.KickoutSessionControlFilter;
import com.acrobat.shiro.realm.MyShiroRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * shiro配置
 * @author xutao
 * @date 2018-11-29 11:29
 */
@Configuration
//@ImportResource(locations={"classpath:shiro/spring-shiro.xml"})
public class ShiroConfig {

    @Autowired
    private FilterChainConfigDao filterChainConfigDao;



    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(ShiroRedisCacheManager cacheManager) {
        MyShiroRealm myShiroRealm = new MyShiroRealm();

        // 指定用户权限缓存管理器
        myShiroRealm.setCacheManager(cacheManager);

        return myShiroRealm;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(MyShiroRealm myShiroRealm,
                                                               DefaultWebSessionManager sessionManager) {
        // shiro提供DefaultSecurityManager, DefaultWebSecurityManager两个实现，分别用于SE环境和web环境
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager ();

        securityManager.setRealm(myShiroRealm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                         JWTControlFilter jwtControlFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        shiroFilterFactoryBean.setLoginUrl("/login");                       // 没通过authc过滤器校验的请求会跳转到这个地址

//        // 加载自定义filter
//        Map<String, Filter> filtersMap = new LinkedHashMap<>();
//        filtersMap.put("jwt", jwtControlFilter);                            // 注意filter的key要和数据库配置一致
//        shiroFilterFactoryBean.setFilters(filtersMap);

        // 从数据库加载FilterChain配置，有序 && 大小写敏感
        Map<String, String> filterChainDefinitionMap = filterChainConfigDao.loadFilterChainDefinitions();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /* shiro用户权限缓存 */
    @Bean
    public ShiroRedisCacheManager getShiroRedisCacheManager(RedisTemplate redisTemplate) {

        return new ShiroRedisCacheManager(redisTemplate);
    }


    /* session缓存Dao，shiro的session没法用可读的序列化方式，所以不需要缓存了... */
    @Bean(name = "redisSessionDao")
    public RedisSessionDao getRedisSessionDao() {

        return new RedisSessionDao();
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getDefaultWebSessionManager(RedisSessionDao sessionDao) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);                            // 全局session失效时间（ms），默认30分钟
        sessionManager.setSessionValidationInterval(1800000);

        sessionManager.setSessionIdCookieEnabled(true);                             // 开启Cookie
        sessionManager.getSessionIdCookie().setName("my_session_id");               // Cookie中sessionId的key
        sessionManager.getSessionIdCookie().setHttpOnly(true);

        sessionManager.setSessionIdUrlRewritingEnabled(false);                      // 避免url中带上sessionId

        // shiro session缓存，可以去掉
        sessionManager.setSessionDAO(sessionDao);
        return sessionManager;
    }

    /* 自定义Filter */
    @Bean(name = "kickoutSessionControlFilter")
    public KickoutSessionControlFilter getKickoutSessionControlFilter(DefaultWebSessionManager sessionManager,
                                                                      ShiroRedisCacheManager cacheManager) {
        return new KickoutSessionControlFilter(sessionManager, cacheManager);
    }

    @Bean
    public JWTControlFilter getJWTControlFilter() {

        return new JWTControlFilter();
    }
}
