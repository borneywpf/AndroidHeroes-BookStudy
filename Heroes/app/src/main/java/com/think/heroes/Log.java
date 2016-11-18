package com.think.heroes;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by borney on 11/18/16.
 */
public final class Log {
    private static final String TAG = "Heroes_";

    public static void d(String tag, String msg) {
        android.util.Log.d(TAG + tag, msg);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(TAG + tag, msg);
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(TAG + tag, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(TAG + tag, msg);
    }

    public static void e(String tag, Throwable thx) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        thx.printStackTrace(pw);
        android.util.Log.e(TAG + tag, sw.toString());
    }
}
