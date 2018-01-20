package net.jiaobaowang.visitor.entity;

/**
 * Created by rocka on 2018/1/19.
 */

public class ShakeHandResult {
    private String RspCode;
    private ShakeHandData RspData;
    private String RspTxt;

    public String getRspCode() {
        return RspCode;
    }

    public void setRspCode(String rspCode) {
        RspCode = rspCode;
    }

    public ShakeHandData getRspData() {
        return RspData;
    }

    public void setRspData(ShakeHandData rspData) {
        RspData = rspData;
    }

    public String getRspTxt() {
        return RspTxt;
    }

    public void setRspTxt(String rspTxt) {
        RspTxt = rspTxt;
    }
}
