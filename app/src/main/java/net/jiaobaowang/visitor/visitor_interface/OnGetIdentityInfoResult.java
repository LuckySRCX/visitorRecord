package net.jiaobaowang.visitor.visitor_interface;

import android.graphics.Bitmap;

import com.telpo.tps550.api.idcard.IdentityInfo;

/**
 * 返回身份证信息和身份证头像
 * Created by ShangLinMo on 2018/1/20.
 */

public interface OnGetIdentityInfoResult {
    /**
     * 返回身份证信息和身份证头像
     *
     * @param code          0，失败；1，成功
     * @param msg           失败信息
     * @param identityInfo  身份证信息
     * @param identityImage 身份证头像
     */
    void getIdentityInfoResult(int code, String msg, IdentityInfo identityInfo, Bitmap identityImage);
}
