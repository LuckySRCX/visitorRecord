package net.jiaobaowang.visitor.entity;

import java.io.Serializable;

/**
 * 门卫
 * Created by ShangLinMo on 2018/1/5.
 */

public class Guard implements Serializable{
    private String id;//id
    private String name;//姓名

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
