package com.acrobat.shiro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author xutao
 * @date 2018-11-30 10:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void test1() {

        // 查看LettuceRedisConfig类的配置

        redisTemplate.opsForHash().put("hash", "key", "value");
        redisTemplate.opsForValue().setIfAbsent("lock:test", "aaa");
        redisTemplate.expire("lock:test", 30, TimeUnit.SECONDS);

        // 只有使用StringRedisSerializer序列化器才能使用increment方法！
        stringRedisTemplate.opsForValue().set("number", "12.3");
        stringRedisTemplate.opsForValue().increment("number", 2.7);
        stringRedisTemplate.opsForValue().increment("number", 2.7);

        // redisTemplate的value是Object
        Double value = (Double) redisTemplate.opsForValue().get("number");
        if (value != null) {
            System.out.println(value);
        }


    }

    @Test
    public void test2() {
        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();

        String key = "shiro_redis_session:44f7d9d4-a101-42cb-aea5-753166da00df";
        redisTemplate.opsForValue().get(key);

        serializer.deserialize(key.getBytes(Charset.forName("UTF-8")));
    }
}
