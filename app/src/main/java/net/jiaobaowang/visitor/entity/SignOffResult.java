package net.jiaobaowang.visitor.entity;

/**
 * Created by rocka on 2018/1/18.
 */

public class SignOffResult {
    private String msg;//:签离成功,
    private String code;//:0000,
    private String state;//:ok,
    private String id;//:2018000001

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
