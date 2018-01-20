package net.jiaobaowang.visitor.entity;

import net.jiaobaowang.visitor.base.FlagObject;

/**
 * Created by rocka on 2018/1/18.
 */

public class ShakeHandData extends FlagObject {
    private String Exponent;
    private String Modulus;

    public String getExponent() {
        return Exponent;
    }

    public void setExponent(String exponent) {
        Exponent = exponent;
    }

    public String getModulus() {
        return Modulus;
    }

    public void setModulus(String modulus) {
        Modulus = modulus;
    }
}
