package net.jiaobaowang.visitor.entity;

/**
 * 访客添加的回调
 * Created by ShangLinMo on 2018/1/16.
 */

public class AddFormResult {
    private String code;
    private String state;
    private String msg;
    private VisitRecord visitor;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public VisitRecord getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitRecord visitor) {
        this.visitor = visitor;
    }
}
