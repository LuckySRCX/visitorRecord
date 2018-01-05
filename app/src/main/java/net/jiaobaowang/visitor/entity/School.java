package net.jiaobaowang.visitor.entity;

/**
 * 学校
 * Created by ShangLinMo on 2018/1/5.
 */

public class School {
    private String id;//id
    private String name;//名称

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

    @Override
    public String toString() {
        return name;
    }
}
