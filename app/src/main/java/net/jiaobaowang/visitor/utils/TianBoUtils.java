package net.jiaobaowang.visitor.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * 天波的公共方法
 * Created by ShangLinMo on 2018/1/20.
 */

public class TianBoUtils {

    /**
     * 检查天波的API
     *
     * @param activity
     * @param packageName
     * @return
     */
    public static boolean checkPackage(Activity activity, String packageName) {
        PackageManager manager = activity.getPackageManager();
        Intent intent = new Intent().setPackage(packageName);
        @SuppressLint("WrongConstant") List<ResolveInfo> infos = manager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        if (infos == null || infos.size() < 1) {
            return false;
        }
        return true;
    }
}
