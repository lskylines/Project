package cn.nowcode.exception;

import cn.nowcode.result.CodeMsg;

/**
 * @author
 * @create 2019-07-13 23:44
 */
public class GlobalException  extends  RuntimeException{
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
