package net.jiaobaowang.visitor.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rocka on 2018/1/16.
 */

public class TimeFormat {
    public static String formatTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

    public static Date getDateFromFormatTime(String formTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date;
        try {
            date = format.parse(formTime);
        } catch (Exception e) {
            Log.e("ERROR", "初始话失败");
            date = new Date();
        }
        return date;
    }
}
