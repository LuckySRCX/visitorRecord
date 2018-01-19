package net.jiaobaowang.visitor.entity;

/**
 * Created by ShangLinMo on 2018/1/18.
 */

public class QiNiuCommand {
    private String Bucket;
    private String Key;
    private String Pops;
    private String NotifyUrl;

    public QiNiuCommand() {
    }

    public QiNiuCommand(String Bucket, String Key, String Pops, String NotifyUrl) {
        this.Bucket = Bucket;
        this.Key = Key;
        this.Pops = Pops;
        this.NotifyUrl = NotifyUrl;
    }

    public String getBucket() {
        return Bucket;
    }

    public void setBucket(String bucket) {
        Bucket = bucket;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPops() {
        return Pops;
    }

    public void setPops(String pops) {
        Pops = pops;
    }

    public String getNotifyUrl() {
        return NotifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        NotifyUrl = notifyUrl;
    }
}
