package net.jiaobaowang.visitor.entity;

/**
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolGradeResult {
    private String RspCode;
    private String RspTxt;
    private SchoolGradeRspData RspData;

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

    public SchoolGradeRspData getRspData() {
        return RspData;
    }

    public void setRspData(SchoolGradeRspData rspData) {
        RspData = rspData;
    }
}
