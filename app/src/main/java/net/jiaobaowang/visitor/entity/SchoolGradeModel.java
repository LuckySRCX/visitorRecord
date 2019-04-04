package net.jiaobaowang.visitor.entity;

/**
 * 年级
 * Created by rocka on 2018/1/22.
 */

public class SchoolGradeModel {
    private int grdid;//年级ID
    private int grdcode;//年级代码
    private String grdname;//年级名称
    private int pcode;//父节点ID
    private int isfinish;

    @Override
    public String toString() {
        return grdname;
    }

    public int getGrdcode() {
        return grdcode;
    }

    public void setGrdcode(int grdcode) {
        this.grdcode = grdcode;
    }

    public String getGrdname() {
        return grdname;
    }

    public void setGrdname(String grdname) {
        this.grdname = grdname;
    }

    public int getPcode() {
        return pcode;
    }

    public void setPcode(int pcode) {
        this.pcode = pcode;
    }

    public int getGrdid() {
        return grdid;
    }

    public void setGrdid(int grdid) {
        this.grdid = grdid;
    }

    public int getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(int isfinish) {
        this.isfinish = isfinish;
    }
}
