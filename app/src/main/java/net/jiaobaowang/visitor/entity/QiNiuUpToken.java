package net.jiaobaowang.visitor.entity;

/**
 * 七牛上传token
 * Created by ShangLinMo on 2018/1/24.
 */

public class QiNiuUpToken {
    private String P_Bucket;
    private String P_Key;
    private String Token;
    private String Domain;
    private String Key;
    private Object OtherKey;

    public String getP_Bucket() {
        return P_Bucket;
    }

    public void setP_Bucket(String p_Bucket) {
        P_Bucket = p_Bucket;
    }

    public String getP_Key() {
        return P_Key;
    }

    public void setP_Key(String p_Key) {
        P_Key = p_Key;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Object getOtherKey() {
        return OtherKey;
    }

    public void setOtherKey(Object otherKey) {
        OtherKey = otherKey;
    }
}
