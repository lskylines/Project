package cn.itcast.Interceptor;

import cn.itcast.dao.LoginTicketDAO;
import cn.itcast.model.HostHolder;
import cn.itcast.model.LoginTikcet;
import cn.itcast.model.User;
import cn.itcast.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author
 * @create 2019-06-28 20:26
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        logger.info("进入Passport拦截器的PreHandle中");
        String ticket = null;
        if(request.getCookies()!=null){
            for(Cookie c:request.getCookies()){
                if("ticket".equals(c.getName())){
                    ticket = c.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){
            LoginTikcet loginTikcet = userService.selectByTicket(ticket);
            //通过ticket查找到的LoginTicket为空 | 已过期 | status!=0
            if(loginTikcet==null || loginTikcet.getExpired().before(new Date()) || loginTikcet.getStatus()!=0){
                return true;
            }
            User user = userService.selectById(loginTikcet.getUserId());
            hostHolder.set(user);
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //后端与前端的切入点
        if(modelAndView!=null && hostHolder.get()!=null){
            modelAndView.addObject("user", hostHolder.get());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.info("进入PassportInterceptro中的afterCompletion中");
        hostHolder.clear();
    }
}
