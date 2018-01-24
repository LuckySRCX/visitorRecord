package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * 查询部门成员返回值的RspData
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolDepartUserRspData {
    private List<SchoolDepartUserModel> users;//部门成员列表

    public List<SchoolDepartUserModel> getUsers() {
        return users;
    }

    public void setUsers(List<SchoolDepartUserModel> users) {
        this.users = users;
    }
}
