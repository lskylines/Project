package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-18 15:28
 */
public class AccessKey  extends BasePrefix{
    public AccessKey(String Prefix) {
        super(Prefix);
    }

    public AccessKey(int expireSeconds, String Prefix) {
        super(expireSeconds, Prefix);
    }
    public static final AccessKey getAccessKey = new AccessKey(5, "ak");
}
