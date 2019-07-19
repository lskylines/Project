package cn.nowcode.service;

import cn.nowcode.domain.*;
import cn.nowcode.redis.MiaoshaKey;
import cn.nowcode.redis.RedisService;
import cn.nowcode.util.MD5Util;
import cn.nowcode.util.UUIDUtil;
import cn.nowcode.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author
 * @create 2019-07-14 16:46
 */
@Service
public class MiaoshaService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    private static char[] ops = new char[]{'+', '-', '*'};

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减库存，下订单，生成订单
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        miaoshaGoods.setStockCount(goodsVo.getStockCount());
        boolean flag = goodsService.reduceStockCount(miaoshaGoods);
        if (flag) {
            OrderInfo orderInfo = orderService.createOrder(user, goodsVo);
            return orderInfo;
        } else {
            setGoodOver(goodsVo.getId());
            return null;
        }
    }

    public long getMiaoshaResult(long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public void setGoodOver(long goodsId) {
        redisService.set(MiaoshaKey.getmiaoshaKey, "" + goodsId, true);
    }

    public boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.getmiaoshaKey, "" + goodsId);
    }

    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId()+"_"+goodsId,str);
        return str;
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if(user==null || path==null){
            return false;
        }
        String pathOld =  redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId()+"_"+goodsId,String.class);
        return path.equals(pathOld);
    }

    public BufferedImage createImage(MiaoshaUser user, long goodsId) {
        if(user==null || goodsId<=0){
            return null;
        }
        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String generateVerifyCode(Random random){
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        char op1 = ops[random.nextInt(3)];
        char op2 = ops[random.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if(user==null || goodsId <= 0){
            return false;
        }
        Integer oldCode =  redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(oldCode==null || verifyCode - oldCode!=0){
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return  true;
    }
}
