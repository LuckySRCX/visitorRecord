package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * 学校用户
 * Created by ShangLinMo on 2018/1/5.
 */

public class User implements Serializable {
    private String userId;//用户id
    private String userName;//用户名称
    private String userType;//类型：0，教职工；1，学生
    private String departId;//部门id
    private String gradeId;//年级id
    private String classId;//班级id

    @Override
    public String toString() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
