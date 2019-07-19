package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-13 20:47
 */
public class UserKey extends BasePrefix{
    public static final int COOKIE_EXPIRED =  3600 * 24 * 2;

    private UserKey( String Prefix) {
        super(0, Prefix);
    }


    private UserKey(int expireSeconds, String Prefix) {
        super(expireSeconds, Prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
    public static UserKey getByToken = new UserKey(COOKIE_EXPIRED, "token");
}
