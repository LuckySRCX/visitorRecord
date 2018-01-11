package net.jiaobaowang.visitor.entity;

/**
 * Created by rocka on 2018/1/10.
 */

public class LoginResult {
    private String code;
    private String state;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
