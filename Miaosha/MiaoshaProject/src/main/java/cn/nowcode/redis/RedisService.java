package cn.nowcode.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author
 * @create 2019-07-13 14:47
 */
@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;


    public <T> T get(KeyPrefix userKey, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = userKey.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = StringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }
    public boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }
    public <T> Boolean set(KeyPrefix userKey, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = BeanToString(value);
            if(str==null || str.length() < 0){
                return false;
            }
            String realKey = userKey.getPrefix() + key;
            int second = userKey.getexpireSeconds();
            if(second <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, second, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean  exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String BeanToString(T value){
        if(value == null)
            return null;
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String)value;
        }else if(clazz==long.class || clazz==Long.class){
            return "" + value;
        }
        return JSON.toJSONString(value);
    }

    public static <T> T StringToBean(String str, Class<T> clazz){
        if(str==null || str.length() <0 || clazz==null)
            return null;
        if(clazz==int.class || clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class || clazz==Long.class){
            return (T)Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }
}
