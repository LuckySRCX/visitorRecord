package net.jiaobaowang.visitor.entity;

/**
 * 老师
 * Created by rocka on 2018/1/22.
 */

public class SchoolClassTeaModel {
    private int utid;//用户ID
    private String utname;//姓名
    private int clsid;//班级ID
    private String clsname;//班级名称
    private int isms;//是否是班主任
    private String grdcode;//年级代码
    private String subcode;//科目代码
    private String subname;//科目名称

    @Override
    public String toString() {
        return utname;
    }

    public int getUtid() {
        return utid;
    }

    public void setUtid(int utid) {
        this.utid = utid;
    }

    public String getUtname() {
        return utname;
    }

    public void setUtname(String utname) {
        this.utname = utname;
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

    public int getIsms() {
        return isms;
    }

    public void setIsms(int isms) {
        this.isms = isms;
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

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }
}
