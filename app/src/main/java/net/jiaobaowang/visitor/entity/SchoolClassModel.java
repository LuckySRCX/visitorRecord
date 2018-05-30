package net.jiaobaowang.visitor.entity;

/**
 * 班级
 * Created by rocka on 2018/1/22.
 */

public class SchoolClassModel {
    private int  grdid;
    private String  grdcode;
    private String subcode;
    private int clsid;
    private int isms;
    private String clsname;
    private int isfinish;

    public int getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(int isfinish) {
        this.isfinish = isfinish;
    }

    @Override
    public String toString() {
        return clsname;
    }

    public String getGrdcode() {
        return grdcode;
    }

    public void setGrdcode(String grdcode) {
        this.grdcode = grdcode;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public int getClsid() {
        return clsid;
    }

    public void setClsid(int clsid) {
        this.clsid = clsid;
    }

    public int getGrdid() {
        return grdid;
    }

    public void setGrdid(int grdid) {
        this.grdid = grdid;
    }

    public int getIsms() {
        return isms;
    }

    public void setIsms(int isms) {
        this.isms = isms;
    }

    public String getClsname() {
        return clsname;
    }

    public void setClsname(String clsname) {
        this.clsname = clsname;
    }
}
