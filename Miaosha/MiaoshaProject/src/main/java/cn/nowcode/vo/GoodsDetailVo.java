package cn.nowcode.vo;

import cn.nowcode.domain.MiaoshaUser;

/**
 * @author
 * @create 2019-07-15 10:11
 */
public class GoodsDetailVo {
    private MiaoshaUser user;
    private GoodsVo goods;
    private int miaoshaStatus;
    private int remainSeconds;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
