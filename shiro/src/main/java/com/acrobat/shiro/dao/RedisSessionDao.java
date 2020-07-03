package com.acrobat.shiro.dao;

import com.acrobat.shiro.constant.SessionAttributeKeys;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * shiro实现Redis缓存session的管理操作
 * @author xutao
 * @date 2018-12-03 09:55
 */
@SuppressWarnings("all")
public class RedisSessionDao extends AbstractSessionDAO {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static int SESSION_VALID_MINUTES = 60;
    private final static String PREFIX = "shiro_redis_session:";

    @Resource(name = "sessionRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    private JSONSerializer serializer;


    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        // 保存缓存时，设置有效时间
        redisTemplate.opsForValue().set(PREFIX + sessionId, session, SESSION_VALID_MINUTES, TimeUnit.MINUTES);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = (Session) redisTemplate.opsForValue().get(PREFIX + sessionId);

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        Serializable sessionId = session.getId();
        if (sessionId != null) {
            // 保存缓存时，设置有效时间
            redisTemplate.opsForValue().set(PREFIX + sessionId, session, SESSION_VALID_MINUTES, TimeUnit.MINUTES);
        }
    }

    @Override
    public void delete(Session session) {

        redisTemplate.delete(PREFIX + session.getId());
    }

    /**
     * 获取当前所有活跃session，可以判断当前在线人数
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<Object> keys = redisTemplate.keys(PREFIX + "*");

        for (Object key : keys) {
            Session session = (Session) redisTemplate.opsForValue().get(key);
            sessions.add(session);
        }
        return sessions;
    }


    /**
     * 自定义序列化session方法
     */
    private String serializeSession(Session session) {
        // 除了自定义的attribute，其它都删掉
        List<Object> attrKeys = new ArrayList<>(session.getAttributeKeys());
        Set<String> wantedKeys = SessionAttributeKeys.getAllKeys();
        for (Object key : attrKeys) {
            if (!wantedKeys.contains(key)) {
                session.removeAttribute(key);
            }
        }

        // 使用fastjson手动序列化成String保存
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.SkipTransientField, false);
        serializer.config(SerializerFeature.IgnoreNonFieldGetter, true);
        serializer.write(session);

        return out.toString();
    }
}
