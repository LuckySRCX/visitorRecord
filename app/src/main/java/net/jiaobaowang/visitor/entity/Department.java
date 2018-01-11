package net.jiaobaowang.visitor.entity;

/**
 * 部门
 * Created by ShangLinMo on 2018/1/11.
 */

public class Department {
    private String departId;//部门id
    private String departName;//部门名称

    public Department() {
    }

    public Department(String id, String name) {
        this.departId = id;
        this.departName = name;
    }

    @Override
    public String toString() {
        return departName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }
}
