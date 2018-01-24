package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolClassStuRspData {
    private List<SchoolClassStuModel> clssstus;//班级学生列表

    public List<SchoolClassStuModel> getClssstus() {
        return clssstus;
    }

    public void setClssstus(List<SchoolClassStuModel> clssstus) {
        this.clssstus = clssstus;
    }
}
