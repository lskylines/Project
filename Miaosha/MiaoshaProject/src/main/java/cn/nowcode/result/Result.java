package cn.nowcode.result;

/**
 * @author
 * @create 2019-07-13 10:43
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }
    public Result(CodeMsg codeMsg){
        if(codeMsg==null)
            return ;
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public Result(T data){
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
