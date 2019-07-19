package cn.itcast.service;

import cn.itcast.Utils.JedisAdapter;
import cn.itcast.Utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * @create 2019-07-07 9:32
 */
@Service
public class LikeService {
    @Autowired
    private JedisAdapter jedisAdapter;
    public int getLikeStatus(int userId,int entityId, int entityType){
        String likeKey = RedisKeyUtils.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityId, entityType);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1:0;
    }

     public long like(int userId,int entityId, int entityType){
        String likeKey = RedisKeyUtils.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String dislikeKey = RedisKeyUtils.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
     }
     public long disLike(int userId, int entityId, int entityType){
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        String likeKey = RedisKeyUtils.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
     }
}
