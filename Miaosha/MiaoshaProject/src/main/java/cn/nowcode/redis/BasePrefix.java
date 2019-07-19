package cn.nowcode.redis;

/**
 * @author
 * @create 2019-07-13 20:45
 */
public abstract  class BasePrefix implements KeyPrefix{
    public int expireSeconds;
    public String Prefix;

    public BasePrefix(String Prefix) {
        this(0, Prefix);
    }

    public BasePrefix(int expireSeconds, String Prefix) {
        this.expireSeconds = expireSeconds;
        this.Prefix = Prefix;
    }

    @Override
    public int getexpireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + Prefix;
    }
}
