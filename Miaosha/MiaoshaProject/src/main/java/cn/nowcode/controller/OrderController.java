package cn.nowcode.controller;

import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.domain.OrderInfo;
import cn.nowcode.result.CodeMsg;
import cn.nowcode.result.Result;
import cn.nowcode.service.GoodsService;
import cn.nowcode.service.OrderService;
import cn.nowcode.vo.GoodsVo;
import cn.nowcode.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @create 2019-07-15 14:51
 */
@Controller
@RequestMapping(path="/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @RequestMapping(path="/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user, @RequestParam("orderId")long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getByOrderId(orderId);
        if(order==null){
            return Result.error(CodeMsg.ORDER_NOT_EXISTS);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoods(goodsVo);
        return Result.success(detail);
    }
}
