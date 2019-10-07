package cn.itcast.Utils;

/**
 * @author
 * @create 2019-07-07 9:26
 */
public class RedisKeyUtils {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";

    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    public static String getLikeKey(int entityId, int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
}
