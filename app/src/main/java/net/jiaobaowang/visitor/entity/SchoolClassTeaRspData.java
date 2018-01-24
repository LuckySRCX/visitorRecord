package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolClassTeaRspData {
    private List<SchoolClassTeaModel> clssusers;//班级学生列表

    public List<SchoolClassTeaModel> getClssusers() {
        return clssusers;
    }

    public void setClssusers(List<SchoolClassTeaModel> clssusers) {
        this.clssusers = clssusers;
    }
}
