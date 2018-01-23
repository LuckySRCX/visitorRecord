package net.jiaobaowang.visitor.entity;

/**
 * Created by rocka on 2018/1/16.
 */

public class ListResult {
    private String code;
    private ListData data;
    private String stat;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ListData getData() {
        return data;
    }

    public void setData(ListData data) {
        this.data = data;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
