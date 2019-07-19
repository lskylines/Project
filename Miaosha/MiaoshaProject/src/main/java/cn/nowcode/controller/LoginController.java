package cn.nowcode.controller;

import cn.nowcode.result.CodeMsg;
import cn.nowcode.result.Result;
import cn.nowcode.service.MiaoshaUserService;
import cn.nowcode.util.ValidatorUtil;
import cn.nowcode.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author
 * @create 2019-07-13 22:15
 */
@Controller
@RequestMapping(path="/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @RequestMapping(path="/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping(path="/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo, HttpServletResponse response){
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        miaoshaUserService.login(loginVo, response);
       return Result.success(true);
    }
}
