package com.acrobat.shiro.config;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于RedisTemplate的shiro缓存实现
 * @author xutao
 * @date 2018-11-30 14:45
 */
@SuppressWarnings("all")
public class ShiroRedisCache<K,V> implements Cache<K,V> {

    private RedisTemplate redisTemplate;
    /** 缓存的redis key前缀 */
    private String prefix = "shiro_redis";

    public String getPrefix() {
        return prefix + ":";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ShiroRedisCache(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public ShiroRedisCache(RedisTemplate redisTemplate, String prefix){
        this(redisTemplate);
        this.prefix = prefix;
    }



    @Override
    public V get(K key) throws CacheException {
        if (key == null) {
            return null;
        }
        return (V)redisTemplate.opsForValue().get(getPrefix() + key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (key == null || value == null) {
            return null;
        }

        // 保存缓存时，设置有效时间
        redisTemplate.opsForValue().set(getPrefix() + key, value, 30, TimeUnit.MINUTES);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        if (key == null) {
            return null;
        }
        V v = (V)redisTemplate.opsForValue().get(getPrefix() + key);
        redisTemplate.delete(getPrefix() + key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
//        // 这个方法是清除DB所有key，慎用
//        redisTemplate.getConnectionFactory().getConnection().flushDb();

        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return redisTemplate.getConnectionFactory().getConnection().dbSize().intValue();
    }

    @Override
    public Set<K> keys() {
        Set<K> keys = redisTemplate.keys(getPrefix() + "*");

        return keys;
    }

    @Override
    public Collection<V> values() {
        Set<K> keys = keys();
        List<V> values = new ArrayList<>(keys.size());
        for (K k : keys) {
            values.add(get(k));
        }
        return values;
    }
}
