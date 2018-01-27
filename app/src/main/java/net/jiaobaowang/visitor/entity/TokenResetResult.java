package net.jiaobaowang.visitor.entity;

/**
 * Token续订接口的返回值
 * Created by ShangLinMo on 2018/1/27.
 */

public class TokenResetResult {
    private String RspCode;
    private String RspTxt;
    private String RspData;

    public String getRspCode() {
        return RspCode;
    }

    public void setRspCode(String rspCode) {
        RspCode = rspCode;
    }

    public String getRspTxt() {
        return RspTxt;
    }

    public void setRspTxt(String rspTxt) {
        RspTxt = rspTxt;
    }

    public String getRspData() {
        return RspData;
    }

    public void setRspData(String rspData) {
        RspData = rspData;
    }
}
