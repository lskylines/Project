package cn.itcast.model;

/**
 * @author
 * @create 2019-07-06 9:14
 */
public class NewsUser extends  News{
    private String name;
    private String headUrl;
    private int like;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
