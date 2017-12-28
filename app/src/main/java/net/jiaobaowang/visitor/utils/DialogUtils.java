package net.jiaobaowang.visitor.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by ShangLinMo on 2017/12/28.
 */

public class DialogUtils {
    public static void showAlert(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(message)
                .setNegativeButton("确定", null)
                .show();
    }

    public static void showAlert(Context context, int id) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(context.getResources().getString(id))
                .setNegativeButton("确定", null)
                .show();
    }
}
