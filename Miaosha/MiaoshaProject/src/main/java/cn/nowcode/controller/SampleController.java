package cn.nowcode.controller;

import cn.nowcode.domain.User;
import cn.nowcode.rabbitmq.MQSender;
import cn.nowcode.redis.RedisService;
import cn.nowcode.redis.UserKey;
import cn.nowcode.result.CodeMsg;
import cn.nowcode.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @create 2019-06-15 15:51
 */
@Controller
public class SampleController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;




    @RequestMapping("/")
    @ResponseBody
    public Result<String> home(){
        return Result.success("asdas");
    }

    @RequestMapping(path="/err")
    @ResponseBody
    public Result<String> error(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }


    @RequestMapping(path="/redisGet")
    @ResponseBody
    public Result<User> redisGet(){
       User user = redisService.get(UserKey.getById, ""+1, User.class);
        return new Result<User>(user);
    }

    @RequestMapping(path = "/redisSet")
    @ResponseBody
    public Result<Boolean> redisSet(){
        Boolean flag = redisService.set(UserKey.getById, ""+2, 123);
        return new Result<Boolean>(flag);
    }

    @RequestMapping(path="/redisexists")
    @ResponseBody
    public Result<Boolean> redisExist(){
        Boolean flag = redisService.exists(UserKey.getById, ""+1);
        return new Result<Boolean>(flag);
    }

    @RequestMapping(path="redisIncr")
    @ResponseBody
    public Result<Long> redisIncr(){
        Long incrValue = redisService.incr(UserKey.getById , ""+2);
        return new Result<Long>(incrValue);
    }

    @RequestMapping(path = "redisDecr")
    @ResponseBody
    public Result<Long> redisDecr(){
        Long decrValue = redisService.decr(UserKey.getById, ""+2);
        return new Result<Long>(decrValue);
    }
}
