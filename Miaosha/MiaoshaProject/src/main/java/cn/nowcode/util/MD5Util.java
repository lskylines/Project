package cn.nowcode.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author
 * @create 2019-07-13 21:59
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    public static String inputPassFormPass(String inputPass){
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }
    public static String formPassToDBPass(String formPassword, String salt){
        return md5(salt+formPassword);
    }
}
