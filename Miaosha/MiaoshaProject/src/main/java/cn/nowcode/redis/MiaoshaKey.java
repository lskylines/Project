package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-15 20:30
 */
public class MiaoshaKey  extends BasePrefix{
    public MiaoshaKey(int expireSeconds, String Prefix) {
        super(expireSeconds, Prefix);
    }

    public MiaoshaKey(String Prefix) {
        super(Prefix);
    }
    public static final MiaoshaKey getmiaoshaKey = new MiaoshaKey("go");
    public static final MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
    public static final MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
}
