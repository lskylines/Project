package cn.itcast.Utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author
 * @create 2019-10-04 10:57
 */
public class ZixunUtils {
        private static final Logger logger = LoggerFactory.getLogger(ZixunUtils.class);
        public static int ANONYMOUS_USERID = 3;    //匿名用户
        public static int SYSTEM_USERID= 0;
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
}
