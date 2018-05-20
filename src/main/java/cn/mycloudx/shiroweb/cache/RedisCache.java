package cn.mycloudx.shiroweb.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;

/**
 * @author zhanghao
 * @date 2018/05/20
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {
    private final RedisTemplate<byte[], byte[]> redisTemplate;

    private final String CACHE_PREFIX="shiro-cache";


    @Autowired
    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    private byte[] getKey(K key){
        if (key instanceof String){
            return (CACHE_PREFIX+key).getBytes();
        }
        return SerializationUtils.serialize(key);
    }


    @Override
    public V get(K key) throws CacheException {

        return null;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        return null;
    }

    @Override
    public V remove(K key) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
