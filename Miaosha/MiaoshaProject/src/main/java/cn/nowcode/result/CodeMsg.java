package cn.nowcode.result;

/**
 * @author
 * @create 2019-07-13 10:45
 */
public class CodeMsg{
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务器异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg REQUEST_TOO_MANY = new CodeMsg(500103, "请求太频繁");
    //登录模块 5002xx
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500200, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201, "密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500202, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500203, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXISTS = new CodeMsg(500204, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205, "密码错误");


    //商品模块 5003xx

    //订单模块 5004xx
    public static CodeMsg ORDER_NOT_EXISTS = new CodeMsg(500400, "订单不存在");

    //秒杀模块 5005xx
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "秒杀完毕");
    public static CodeMsg REPEAT_MIAOSHA = new CodeMsg(500501, "重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败");

    public CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code,message);
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
