package net.jiaobaowang.visitor.entity;

/**
 * 部门
 * Created by rocka on 2018/1/22.
 */

public class SchoolDepartModel {
    private int pid;//父节点ID
    private int dptid;//部门id
    private String dptname;//部门名称

    @Override
    public String toString() {
        return this.dptname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getDptid() {
        return dptid;
    }

    public void setDptid(int dptid) {
        this.dptid = dptid;
    }

    public String getDptname() {
        return dptname;
    }

    public void setDptname(String dptname) {
        this.dptname = dptname;
    }
}
