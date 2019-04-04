package net.jiaobaowang.visitor.entity;

import net.jiaobaowang.visitor.base.FlagObject;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/5/28.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class SchInfoResult extends FlagObject {
    private String RspCode;
    private String RspTxt;
    private SchInfoSoureRespData RspData;

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


    public SchInfoSoureRespData getRspData() {
        return RspData;
    }

    public void setRspData(SchInfoSoureRespData rspData) {
        RspData = rspData;
    }
}
