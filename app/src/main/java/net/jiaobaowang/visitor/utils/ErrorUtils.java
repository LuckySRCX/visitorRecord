package net.jiaobaowang.visitor.utils;

import java.io.EOFException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 异常错误
 * Created by ShangLinMo on 2018/2/1.
 */

public class ErrorUtils {
    /**
     * 网络连接失败的异常
     *
     * @param e
     * @return
     */
    public static String[] netErrorTranslate(Exception e) {
        String[] error = new String[2];
        error[0] = "0";
        error[1] = e.toString();
        boolean isNetError = false;
        if (e instanceof UnknownHostException || e instanceof SocketException || e instanceof SocketTimeoutException || e instanceof EOFException) {
            isNetError = true;
        }
        if (isNetError) {
            error[1] = "网络连接失败，请检查网络";
        }
        return error;
    }
}
