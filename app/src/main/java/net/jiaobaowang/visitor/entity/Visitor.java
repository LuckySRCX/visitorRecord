package net.jiaobaowang.visitor.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 访客
 * Created by ShangLinMo on 2018/1/5.
 */

public class Visitor implements Serializable {
    private String name;//姓名
    private String gender;//性别
    private String born;//出生日期
    private String cardType;//证件类型
    private String cardNumber;//证件号码
    private String address;//地址
    private Bitmap image;//照片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name;
    }
}
