package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * 学校用户
 * Created by ShangLinMo on 2018/1/5.
 */

public class User implements Serializable {
    private String id;//用户id
    private String name;//姓名
    private String type;//类型：教职工；学生
    private String department;//部门
    private String gradeName;//年级名称
    private String className;//班级名称
    private String headMaster;//班主任

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHeadMaster() {
        return headMaster;
    }

    public void setHeadMaster(String headMaster) {
        this.headMaster = headMaster;
    }

    @Override
    public String toString() {
        return name;
    }
}
