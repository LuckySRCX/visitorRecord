package net.jiaobaowang.visitor.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rocka on 2018/1/22.
 */

public class SharePreferencesUtil {
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    public SharePreferencesUtil(Context context, String fileName) {
        mEditor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
    }

    public SharePreferencesUtil(Context context, String fileName, boolean isPut) {
        mSharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }
}
