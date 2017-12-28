package net.jiaobaowang.visitor.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ShangLinMo on 2017/12/28.
 */

public class ToastUtils {
    public static void showMessage(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMessage(Context context, int id) {
        try {
            Toast.makeText(context, context.getResources().getString(id), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
