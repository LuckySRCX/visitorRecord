package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * 访客单
 * Created by ShangLinMo on 2018/1/3.
 */

public class VisitorForm implements Serializable {
    private Visitor visitor;//访客
    private User user;//被访人
    private Guard guard;//门卫
    private String reason;//事由
    private String phoneNumber;//手机号码
    private String visitorNumber;//访客人数
    private String belongings;//随身物品
    private String organization;//单位名称
    private String plateNumber;//车牌号
    private String remarks;//备注
    private String registerTime;//登记时的时间
    private String entryTime;//进入的时间
    private String leaveTime;//离开的时间

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Guard getGuard() {
        return guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVisitorNumber() {
        return visitorNumber;
    }

    public void setVisitorNumber(String visitorNumber) {
        this.visitorNumber = visitorNumber;
    }

    public String getBelongings() {
        return belongings;
    }

    public void setBelongings(String belongings) {
        this.belongings = belongings;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }
}
