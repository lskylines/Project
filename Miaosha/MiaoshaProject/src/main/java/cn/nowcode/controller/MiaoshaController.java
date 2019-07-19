package cn.nowcode.controller;

import cn.nowcode.domain.MiaoshaOrder;
import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.domain.OrderInfo;
import cn.nowcode.rabbitmq.MQSender;
import cn.nowcode.rabbitmq.MiaoshaMessage;
import cn.nowcode.redis.AccessKey;
import cn.nowcode.redis.GoodsKey;
import cn.nowcode.redis.MiaoshaKey;
import cn.nowcode.redis.RedisService;
import cn.nowcode.result.CodeMsg;
import cn.nowcode.result.Result;
import cn.nowcode.service.GoodsService;
import cn.nowcode.service.MiaoshaService;
import cn.nowcode.service.OrderService;
import cn.nowcode.util.MD5Util;
import cn.nowcode.util.UUIDUtil;
import cn.nowcode.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author
 * @create 2019-07-14 16:12
 */
@Controller
@RequestMapping(path = "/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;
    public Map<Long,Boolean> map = new HashMap<>();
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodList = goodsService.listGoodsVo();
        if(goodList==null){
            return ;
        }
        for(GoodsVo goods: goodList){
            redisService.set(GoodsKey.getGoodsStock, ""+goods.getId(), goods.getStockCount());
            map.put(goods.getId() , false);
        }
    }


    //优化后
    //5000b并发量 QPS:2100
    @RequestMapping(path="/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doMiaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId")long goodsId,
                                     @PathVariable("path")String path){
        model.addAttribute("user", user);
        //判断用户是否已经登录
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证Path
        boolean check = miaoshaService.checkPath(user,goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //当库存量小于0时,拦截住后面的请求去缓存取数据
        if(map.get(goodsId)){
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, ""+goodsId);
        if(stock < 0){
            map.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否已经秒杀
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder!=null){
            //model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
            //return "miaosha_fail";
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }

        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);

        return Result.success(0);
    }


    //并发量5000 QPS:1300
    /*@RequestMapping(path="/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doMiaosha(Model model, MiaoshaUser user,
                            @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        //判断用户是否已经登录
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        int stock = goodsVo.getStockCount();
        if(stock <= 0){
            //model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            //return "miaosha_fail";
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否已经秒杀
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder!=null){
            //model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
            //return "miaosha_fail";
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        //model.addAttribute("orderInfo", orderInfo);
        //model.addAttribute("goods", goodsVo);
        //return "order_detail";
        return Result.success(orderInfo);
    }*/

    /**
     * orderId:成功
     * 1 :秒杀失败
     * 0：排队中
     */
    @RequestMapping(path="/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }



    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //访问次数进行限定
        String uri = request.getRequestURI();
        String key = uri + "_" + user.getId();
        Integer count = redisService.get(AccessKey.getAccessKey, key, Integer.class);
        if(count == null){
            redisService.set(AccessKey.getAccessKey, key, 1);
        }else if(count < 5){
            redisService.incr(AccessKey.getAccessKey, key);
        }else{
            return Result.error(CodeMsg.REQUEST_TOO_MANY);
        }


        boolean check  =miaoshaService.checkVerifyCode(user, goodsId,verifyCode);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    //生成图形验证码
    @RequestMapping(path="/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user,
                                               @RequestParam("goodsId")long goodsId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage image= miaoshaService.createImage(user, goodsId);
        try{
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }


}
