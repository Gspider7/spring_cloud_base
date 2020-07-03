package com.acrobat.shiro.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 使用lettuce连接池时，redis的操作类配置
 * @author xutao
 * @date 2018-11-30 10:23
 */
@Configuration
public class LettuceRedisConfig extends CachingConfigurerSupport {

    /**
     * RedisTemplate自定义配置，覆盖默认配置（主要是序列化配置）
     * RedisAutoConfiguration类自动注入了RedisTemplate和StringRedisTemplate
     * RedisTemplate默认使用的是JdkSerializationRedisSerializer（java对象序列化器，没有可读性，性能也差）
     * StringRedisTemplate默认使用的是StringRedisSerializer
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory connectionFactory,
                                                       Jackson2JsonRedisSerializer serializer) {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);


        // 在调用RedisTemplate相关接口时，参数会被序列化再保存到redis中
        // Jackson2JsonRedisSerializer会把对象转成json，参数可以传任意对象，也保证了可读性
        // StringRedisSerializer是通过String.getBytes来序列化的，只能做用于String参数
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer );                   // 普通key的序列化方式
//        template.setValueSerializer(stringSerializer );                 // 普通value的序列化方式
        template.setHashKeySerializer(stringSerializer );               // hash key的序列化方式
//        template.setHashValueSerializer(stringSerializer );             // hash value的序列化方式

        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 对于shiro的session，所有可读的序列化方式都有问题，所以用默认的JdkSerializationRedisSerializer序列化session
     */
    @Bean(name = "sessionRedisTemplate")
    public RedisTemplate<Object, Object> sessionRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);                    // 普通key的序列化方式
        template.setHashKeySerializer(stringSerializer );               // hash key的序列化方式

        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * shiro用户权限信息保存时的序列化工具
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return jackson2JsonRedisSerializer;
    }
}
