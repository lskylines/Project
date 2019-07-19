package cn.itcast.Utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/3.
 */
public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    public static String[] IMAGE_FILE_EXTS = new String[]{"jpg",  "bmp", "jpeg", "png"};
    public static final String IMG_PATH = "src/main/resources/static/newsImage/";

    //站内信详情页每页显示数量
    public static final int MessagePageNum = 10;
    //评论区每页显示数量
    public static final int CommentPageNum = 10;
    //用于设置首页每页显示数量
    public static final int TotTiaoPageNum = 8;
    public static boolean isAllow(String fileExt){
        for(String IMAGE_FILE_EXT: IMAGE_FILE_EXTS){
            if(IMAGE_FILE_EXT.equals(fileExt)){
                return true;
            }
        }
        return false;
    }

    public static String getJson(int code){
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJson(int code, String msg){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJson(int code, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toString();
    }
   
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
