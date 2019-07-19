package cn.nowcode.controller;

import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.domain.User;
import cn.nowcode.redis.GoodsKey;
import cn.nowcode.redis.KeyPrefix;
import cn.nowcode.redis.RedisService;
import cn.nowcode.result.Result;
import cn.nowcode.service.GoodsService;
import cn.nowcode.service.MiaoshaUserService;
import cn.nowcode.vo.GoodsDetailVo;
import cn.nowcode.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author
 * @create 2019-07-14 10:19
 */
@Controller
@RequestMapping(path = "/goods")
public class GoodController {
    private static final Logger logger = LoggerFactory.getLogger(GoodController.class);
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    //并发量1000时QPS：266
    //并发量2000时QPS:490
    //阿里云并发量10000时,QPS:133
    /*@RequestMapping(path="/to_list", method = RequestMethod.GET)
    public String toList(Model model,
                         MiaoshaUser miaoshaUser) {
        model.addAttribute("user", miaoshaUser);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";

    }*/
    //优化
    //并发量1000QPS: 560 并发量2000 QPS:800 性能提升了65%--75%
    @RequestMapping(path="/to_list", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String to_List(Model model,
                         MiaoshaUser miaoshaUser,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        model.addAttribute("user", miaoshaUser);
        //去缓存中取
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)){
            //存数据进入缓存
            redisService.set(GoodsKey.getGoodsList,"", html);
        }
        return html;
    }

    /*@RequestMapping(path="/to_detail/{goodsId}", method = RequestMethod.GET)
    public String detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId")long goodsId){
        model.addAttribute("user", miaoshaUser);
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt){ //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }else if(now > endAt){ //秒杀结束了
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{ //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }*/
    //优化后
    /*@RequestMapping(path="/to_detail/{goodsId}", method = RequestMethod.GET, produces = "text/html")
    @ResponseBody
    public String detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId")long goodsId,
                         HttpServletRequest request,
                         HttpServletResponse response){
        model.addAttribute("user", miaoshaUser);
        String  html = redisService.get(GoodsKey.getGoodsDetail, ""+goodsId, String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt){ //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }else if(now > endAt){ //秒杀结束了
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{ //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
        }
        return html;
    }*/

    //页面静态优化
    @RequestMapping(path="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, MiaoshaUser miaoshaUser, @PathVariable("goodsId")long goodsId){
        logger.info("进入detail");
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);

        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt){ //秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }else if(now > endAt){ //秒杀结束了
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{ //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setUser(miaoshaUser);
        vo.setGoods(goodsVo);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }

   /* @RequestMapping(path="/to_list", method = RequestMethod.GET)
    public String toList(Model model,
                         @CookieValue(value= "token", required = false)String cookieToken,
                         @RequestParam(value = "token", required = false)String paramToken,
                         MiaoshaUser miaoshaUser){
        *//*if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(cookieToken) ? paramToken: cookieToken;
        MiaoshaUser miaoshaUser = miaoshaUserService.getByToken(token);*//*
        model.addAttribute("user", miaoshaUser);
        return "goods_list";
    }*/
}
