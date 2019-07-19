package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-15 15:11
 */
public class OrderKey extends BasePrefix{
    public OrderKey(int expireSeconds, String Prefix) {
        super(expireSeconds, Prefix);
    }

    public OrderKey(String Prefix) {
        super(Prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
