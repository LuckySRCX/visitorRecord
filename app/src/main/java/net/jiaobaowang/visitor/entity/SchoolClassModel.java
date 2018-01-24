package net.jiaobaowang.visitor.entity;

/**
 * 班级
 * Created by rocka on 2018/1/22.
 */

public class SchoolClassModel {
    private String  grdcode;
    private String subcode;
    private int clsid;
    private int isms;
    private String clsname;

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
