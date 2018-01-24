package net.jiaobaowang.visitor.entity;

/**
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolDepartUserResult {
    private String RspCode;
    private String RspTxt;
    private SchoolDepartUserRspData RspData;

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

    public SchoolDepartUserRspData getRspData() {
        return RspData;
    }

    public void setRspData(SchoolDepartUserRspData rspData) {
        RspData = rspData;
    }
}
