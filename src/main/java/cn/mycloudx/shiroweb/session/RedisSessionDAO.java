package cn.mycloudx.shiroweb.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2018/05/20
 */
@Component
@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO {

    private RedisTemplate<byte[], byte[]> redisTemplate;
    private final String SHIRO_SESSION_PREFIX = "shiro-session";


    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    @Autowired
    public RedisSessionDAO(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);
        saveSession(session);
        return sessionId;
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            redisTemplate.opsForValue().set(key, value, 1000, TimeUnit.SECONDS);
        }

    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.info("doReadSession={}",sessionId);
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = redisTemplate.opsForValue().get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            redisTemplate.delete(key);
        }


    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = redisTemplate.keys((SHIRO_SESSION_PREFIX + "*").getBytes());
        Set<Session> sessionSet = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessionSet;
        }
        keys.forEach(key -> sessionSet.add((Session) SerializationUtils.deserialize(redisTemplate.opsForValue().get(key))));
        return sessionSet;
    }
}
