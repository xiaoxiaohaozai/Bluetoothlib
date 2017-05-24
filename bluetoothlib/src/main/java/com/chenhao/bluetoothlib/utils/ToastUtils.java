package com.chenhao.bluetoothlib.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast统一管理类
 */
public class ToastUtils {

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    private static Toast toast;

    private static Toast getToast(Context context) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */

    public static void show(Context context, int message, int duration) {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 在非ui线程显示
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showNoUIThread(Context context, String message, int duration) {
        if (isShow) {
            Looper.prepare();
            Toast.makeText(context, message, duration).show();
            Looper.loop();
        }
    }


    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 不显示重复的
     *
     * @param context context
     * @param msg     提示信息
     */
    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showCrashToast(Context context, String message) {
        if (true) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
