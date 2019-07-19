package cn.itcast.controller;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author
 * @create 2019-06-22 23:59
 */
@Controller
@RequestMapping(path="/user")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;


    @RequestMapping(path="/register", method = {RequestMethod.POST})
    @ResponseBody
    public String register(@RequestParam(value="username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int remember,
                           HttpServletResponse response){
        Map<String, Object> map = userService.reg(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJson(0, "注册成功");
            } else {
                return ToutiaoUtil.getJson(1, "注册失败");
            }
        }catch (Exception e){
            logger.info("注册异常" + e.getMessage());
            return ToutiaoUtil.getJson(1, "注册异常");
        }
    }

    //用户登录功能
    @RequestMapping(path={"/login"}, method = {RequestMethod.POST})
    @ResponseBody
    public String Register(@RequestParam(value="username") String username,
                           @RequestParam(value="password") String password,
                           @RequestParam(value="rember", defaultValue = "0") int remember,
                           HttpServletResponse response){
        Map<String,Object> map = userService.login(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJson(0, "登录成功");
            } else {
                return ToutiaoUtil.getJson(1, map);
            }
        }catch(Exception e){
            logger.info("登录异常" + e.getMessage());
            return ToutiaoUtil.getJson(1, "登录异常");
        }
    }


    @RequestMapping(path="/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/1";
    }


}
