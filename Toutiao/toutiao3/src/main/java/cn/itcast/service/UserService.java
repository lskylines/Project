package cn.itcast.service;

import cn.itcast.Utils.ToutiaoUtil;
import cn.itcast.dao.LoginTicketDAO;
import cn.itcast.dao.UserDAO;
import cn.itcast.model.LoginTikcet;
import cn.itcast.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author
 * @create 2019-06-22 23:55
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAo;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public int addUser(User user){
        return userDAo.addUser(user);
    }

    public LoginTikcet selectByTicket(String ticket){
        return loginTicketDAO.selectByTicket(ticket);
    }

    public User selectById(int userId){
        return userDAo.selectById(userId);
    }


    //注册
    @Transactional
    public Map<String,Object> reg(String username, String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }
        User user =  userDAo.selectByName(username);
        if(user!=null){
            map.put("msg", "用户已经被注册");
            return map;
        }
        user  = new User();
        user.setSalt(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setName(username);
        user.setPassword(ToutiaoUtil.MD5(user.getSalt()+password));
        userDAo.addUser(user);
        String ticket = addTicket(user.getId());

        map.put("ticket", ticket);
        return map;
    }

    //登录
    @Transactional
    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAo.selectByName(username);
        if(user==null){
            map.put("msg", "用户不存在");
            return map;
        }

        if(!user.getPassword().equals(ToutiaoUtil.MD5(user.getSalt()+password))){
            map.put("msg", "密码不正确");
            return map;
        }
        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);
        return map;

    }
    //登录
    public String addTicket(int userId){
        LoginTikcet loginTikcet = new LoginTikcet();
        loginTikcet.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginTikcet.setExpired(date);
        loginTikcet.setUserId(userId);
        loginTikcet.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTikcet);
        return loginTikcet.getTicket();
    }

    //退出
    public int logout(String ticket){
        int count = loginTicketDAO.logout(ticket);
        return count;
    }
}
