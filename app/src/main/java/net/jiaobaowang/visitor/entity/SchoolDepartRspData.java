package net.jiaobaowang.visitor.entity;

import java.util.List;

/**
 * Created by ShangLinMo on 2018/1/23.
 */

public class SchoolDepartRspData {
    private List<SchoolDepartModel> dpts;//部门列表

    public List<SchoolDepartModel> getDpts() {
        return dpts;
    }

    public void setDpts(List<SchoolDepartModel> dpts) {
        this.dpts = dpts;
    }
}
