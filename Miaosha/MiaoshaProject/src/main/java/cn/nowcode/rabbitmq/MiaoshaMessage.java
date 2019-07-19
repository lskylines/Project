package cn.nowcode.rabbitmq;

import cn.nowcode.domain.MiaoshaUser;

/**
 * @author
 * @create 2019-07-15 19:53
 */
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
