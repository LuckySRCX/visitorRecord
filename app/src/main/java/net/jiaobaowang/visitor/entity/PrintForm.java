package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * 访客单
 * Created by ShangLinMo on 2018/1/3.
 */

public class PrintForm implements Serializable {
    private String formId;//访客单id

    private String visName;//访客姓名
    private int visGender;//访客性别。0，男；1，女
    private String visOrg;//来访单位
    private String visReason;//访问事由

    private int userType;//用户的类型。0，教职工；1，学生；
    private String userName;//用户的名称
    private String userDepartment;//用户的部门
    private String userGradeName;//用户的年级名称
    private String userClassName;//用户的班级名称
    private String userHeadMaster;//用户的班主任姓名

    private String registerName;//登记人姓名
    private String entryTime;//进入的时间
    private String remarks;//备注

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getVisName() {
        return visName;
    }

    public void setVisName(String visName) {
        this.visName = visName;
    }

    public int getVisGender() {
        return visGender;
    }

    public void setVisGender(int visGender) {
        this.visGender = visGender;
    }

    public String getVisOrg() {
        return visOrg;
    }

    public void setVisOrg(String visOrg) {
        this.visOrg = visOrg;
    }

    public String getVisReason() {
        return visReason;
    }

    public void setVisReason(String visReason) {
        this.visReason = visReason;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserGradeName() {
        return userGradeName;
    }

    public void setUserGradeName(String userGradeName) {
        this.userGradeName = userGradeName;
    }

    public String getUserClassName() {
        return userClassName;
    }

    public void setUserClassName(String userClassName) {
        this.userClassName = userClassName;
    }

    public String getUserHeadMaster() {
        return userHeadMaster;
    }

    public void setUserHeadMaster(String userHeadMaster) {
        this.userHeadMaster = userHeadMaster;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
