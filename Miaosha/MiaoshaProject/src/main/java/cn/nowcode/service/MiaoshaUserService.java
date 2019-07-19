package cn.nowcode.service;

import cn.nowcode.dao.MiaoshaUserDao;
import cn.nowcode.domain.MiaoshaUser;
import cn.nowcode.exception.GlobalException;
import cn.nowcode.redis.RedisService;
import cn.nowcode.redis.UserKey;
import cn.nowcode.result.CodeMsg;
import cn.nowcode.util.MD5Util;
import cn.nowcode.util.UUIDUtil;
import cn.nowcode.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 * @create 2019-07-13 22:49
 */
@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    private MiaoshaUserDao miaoshaUserDao;
    @Autowired
    private RedisService redisService;

    public MiaoshaUser getById(long id){
        MiaoshaUser user = redisService.get(UserKey.getById, ""+id, MiaoshaUser.class);
        if(user!=null){
            return user;
        }
        user = miaoshaUserDao.getById(id);
        if(user!=null){
            redisService.set(UserKey.getById, ""+id, user);
        }
        return user;
    }
    public MiaoshaUser getByToken(HttpServletResponse response ,String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(UserKey.getByToken, token, MiaoshaUser.class);
        if(miaoshaUser!=null) {
            addCookie(miaoshaUser, token, response);
        }
        return miaoshaUser;
    }

    //登录
    public boolean login(LoginVo loginVo, HttpServletResponse response){
        if(loginVo==null)
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //判断手机是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if(miaoshaUser==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String dbPassword = MD5Util.formPassToDBPass(password, miaoshaUser.getSalt());
        if(!dbPassword.equals(miaoshaUser.getPassword())){
            throw  new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(miaoshaUser,token, response);
        return true;
    }
    public void addCookie(MiaoshaUser miaoshaUser,String token, HttpServletResponse response){
        redisService.set(UserKey.getByToken, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.getByToken.expireSeconds);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
}

