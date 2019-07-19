package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-13 20:44
 */
public interface KeyPrefix {
    public int getexpireSeconds();

    public String getPrefix();
}
