package cn.itcast.model;

import org.springframework.stereotype.Component;

/**
 * @author
 * @create 2019-06-28 20:24
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();
    public User get(){
        return users.get();
    }
    public void set(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }

}
