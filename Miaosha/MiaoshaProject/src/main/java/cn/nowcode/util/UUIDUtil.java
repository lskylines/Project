package cn.nowcode.util;

import java.util.UUID;

/**
 * @author
 * @create 2019-07-14 10:12
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
