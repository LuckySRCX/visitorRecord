package net.jiaobaowang.visitor.entity;

/**
 * Created by ShangLinMo on 2018/1/16.
 */

public class AddFormResult {
    private String code;
    private String state;
    private String msg;
    private int id;
    private String visit_Int;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisit_Int() {
        return visit_Int;
    }

    public void setVisit_Int(String visit_Int) {
        this.visit_Int = visit_Int;
    }
}
