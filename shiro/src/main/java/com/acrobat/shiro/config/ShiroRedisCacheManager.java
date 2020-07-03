package com.acrobat.shiro.config;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xutao
 * @date 2018-11-30 15:20
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {

    private RedisTemplate redisTemplate;

    /** 保存通过createCache创建的所有缓存 */
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public ShiroRedisCacheManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            cache = new ShiroRedisCache(redisTemplate, name);
            cacheMap.put(name, cache);
        }
        return cache;
    }

    /**
     * 清除当前缓存信息
     */
    public void clear() {
        for (Map.Entry<String, Cache> entry : cacheMap.entrySet()) {
            Cache cache = entry.getValue();
            cache.clear();
        }
    }
}
