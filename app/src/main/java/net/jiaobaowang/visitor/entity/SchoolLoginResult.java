package net.jiaobaowang.visitor.entity;

import net.jiaobaowang.visitor.base.FlagObject;

/**
 * Created by rocka on 2018/1/22.
 */

public class SchoolLoginResult extends FlagObject {
    private String RspCode;
    private String RspTxt;
    private SchoolLoginRspData RspData;

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

    public SchoolLoginRspData getRspData() {
        return RspData;
    }

    public void setRspData(SchoolLoginRspData rspData) {
        RspData = rspData;
    }
}
