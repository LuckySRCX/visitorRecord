package net.jiaobaowang.visitor.base;

import com.google.gson.annotations.Expose;

/**
 * Created by rocka on 2018/1/19.
 */

public abstract class FlagObject extends Object {
    @Expose(deserialize = false, serialize = false)
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
