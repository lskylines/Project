package cn.nowcode.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @create 2019-07-13 22:35
 */
public class ValidatorUtil {
    public static Pattern pattern_mobile = Pattern.compile("1\\d{10}");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher m = pattern_mobile.matcher(mobile);
        return m.matches();
    }
}
