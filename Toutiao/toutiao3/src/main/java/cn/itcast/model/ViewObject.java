package cn.itcast.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @create 2019-06-28 20:14
 */
public class ViewObject {
    public Map<String ,Object> map = new HashMap<>();

    public void set(String key, Object obj){
        map.put(key, obj);
    }
    public Object get(String key){
        return map.get(key);
    }
}
