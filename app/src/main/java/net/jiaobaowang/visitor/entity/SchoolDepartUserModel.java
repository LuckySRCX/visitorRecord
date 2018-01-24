package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * 部门成员
 * Created by rocka on 2018/1/22.
 */

public class SchoolDepartUserModel {
    private int sex;//性别
    private String utname;//用户名
    private int utid;//用户ID
    private List<SchoolDepartModel> udpts;//所在部门

    @Override
    public String toString() {
        return utname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUtname() {
        return utname;
    }

    public void setUtname(String utname) {
        this.utname = utname;
    }

    public int getUtid() {
        return utid;
    }

    public void setUtid(int utid) {
        this.utid = utid;
    }

    public List<SchoolDepartModel> getUdpts() {
        return udpts;
    }

    public void setUdpts(List<SchoolDepartModel> udpts) {
        this.udpts = udpts;
    }
}
