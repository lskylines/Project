package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-14 21:49
 */
public class GoodsKey extends BasePrefix{
    public GoodsKey(String Prefix) {
        super(Prefix);
    }

    public GoodsKey(int expireSeconds, String Prefix) {
        super(expireSeconds, Prefix);
    }
    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
    public static GoodsKey getGoodsStock = new GoodsKey("gs");
}
