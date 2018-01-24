package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * 七牛上传tokenResult
 * Created by ShangLinMo on 2018/1/24.
 */

public class QiNiuUpTokenResult {
    private String Status;
    private String Message;
    private List<QiNiuUpToken> Data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<QiNiuUpToken> getData() {
        return Data;
    }

    public void setData(List<QiNiuUpToken> data) {
        Data = data;
    }
}
