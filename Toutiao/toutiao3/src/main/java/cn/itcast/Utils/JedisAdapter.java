package cn.itcast.Utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author
 * @create 2019-07-07 9:03
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool Pool = null;
    @Override
    public void afterPropertiesSet() throws Exception {
        Pool = new JedisPool();
    }

    public String get(String key){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.get(key);
        }catch(Exception e){
            logger.error("redis Get异常" + e.getMessage());
            return null;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void set(String key,String value){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            jedis.set(key, value);
        }catch(Exception e){
            logger.error("redis set方法异常" +  e.getMessage());
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.sadd(key, value);
        }catch (Exception e){
            logger.error("sadd发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.srem(key, value);
        }catch(Exception e){
            logger.error("Srem发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.sismember(key, value);
        }catch(Exception e){
            logger.error("sismember发生异常" + e.getMessage());
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.scard(key);
        }catch(Exception e){
            logger.error("Scard发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public void set(String key, Object obj){
        set(key, JSON.toJSONString(obj));
    }

    public <T> T getObject(String key, Class<T> clazz){
        String value = get(key);
        if(value!=null){
            return JSON.parseObject(value, clazz);
        }
        return null;
    }

    public long lpush(String key, String value){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.lpush(key, value);
        }catch (Exception e){
            logger.error("lpush发生异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key){
        Jedis jedis = null;
        try{
            jedis = Pool.getResource();
            return jedis.brpop(timeout, key);
        }catch (Exception e){
            logger.error("brpop发生异常" + e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
}
