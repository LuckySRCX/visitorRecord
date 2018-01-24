package net.jiaobaowang.visitor.entity;

/**
 * 学生
 * Created by rocka on 2018/1/22.
 */

public class SchoolClassStuModel {
    private int stuid;//学生ID
    private String stuno;//学号
    private String stuname;//姓名
    private int sex;//性别
    private String grdcode;//年级代码
    private String grdname;//年级名称
    private int clsid;//班级ID
    private String clsname;//班级名称

    @Override
    public String toString() {
        return stuname;
    }

    public int getStuid() {
        return stuid;
    }

    public void setStuid(int stuid) {
        this.stuid = stuid;
    }

    public String getStuno() {
        return stuno;
    }

    public void setStuno(String stuno) {
        this.stuno = stuno;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getGrdcode() {
        return grdcode;
    }

    public void setGrdcode(String grdcode) {
        this.grdcode = grdcode;
    }

    public String getGrdname() {
        return grdname;
    }

    public void setGrdname(String grdname) {
        this.grdname = grdname;
    }

    public int getClsid() {
        return clsid;
    }

    public void setClsid(int clsid) {
        this.clsid = clsid;
    }

    public String getClsname() {
        return clsname;
    }

    public void setClsname(String clsname) {
        this.clsname = clsname;
    }
}
