package com.github.tvbox.osc.util;

import android.util.Log;

import com.github.tvbox.osc.BuildConfig;

/**
 * @author mxd
 * @date :2023/07/25
 * @description:
 */

public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int EVERYTHING = 0;

    private static int LOG_LEVEL = BuildConfig.LOG_DEBUG ? EVERYTHING : ERROR;

    public static void updateLevel(int l) {
        LOG_LEVEL = l;
    }
    public static void v(String msg){
        if(LOG_LEVEL <= VERBOSE){
            Log.v(getTag(), msg);
        }
    }
    public static void v(String tag, String msg){
        if(LOG_LEVEL <= VERBOSE){
            Log.v(getTag(tag), msg);
        }
    }
    public static void d(String msg){
        if(LOG_LEVEL <= DEBUG){
            Log.d(getTag(), msg);
        }
    }
    public static void d(String tag, String msg){
        if(LOG_LEVEL <= DEBUG){
            Log.d(getTag(tag), msg);
        }
    }
    public static void i(String msg){
        if(LOG_LEVEL <= INFO){
            Log.i(getTag(), msg);
        }
    }
    public static void i(String tag, String msg){
        if(LOG_LEVEL <= INFO){
            Log.i(getTag(tag), msg);
        }
    }

    public static void w(String msg){
        if(LOG_LEVEL <= WARN){
            Log.w(getTag(), msg);
        }
    }
    public static void w(String tag, String msg){
        if(LOG_LEVEL <= WARN){
            Log.w(getTag(tag), msg);
        }
    }

    public static void e(String msg){
        e(getTag(), msg);
    }
    public static void e(String tag, String msg){
        if(LOG_LEVEL <= ERROR){
            Log.e(getTag(tag), msg);
        }
    }

    public static void e(Throwable th){
        e(getTag(), th);
    }
    public static void e(String tag, Throwable th){
        if(LOG_LEVEL <= ERROR){
            Log.e(getTag(tag), th.toString());
            th.printStackTrace();
        }
    }
    private static String getTag() {
        // 这里的数组的index，即2，是根据你工具类的层级取的值，可根据需求改变
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return "TVBox_"+result;
    }


    /**
     * 根据类名获取当前调用的方法和行号
     * @param tag 类名
     * @return 前调用的方法和行号
     */
    private static String getTag(String tag) {

        /* 获取该线程堆栈存储的堆栈跟踪元素数组。如果该线程尚未启动或已经终止，则该方法将返回一个零长度数组。
        如果返回的数组不是零长度的，最后一个元素代表堆栈底。*/

        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        // temp[0] 表示getStackTrace方法，故排除，此处也可从下标记2取做默认值
        StackTraceElement method = temp[1];

        // 由于数组的第一个元素代表栈顶，它是该序列中最新的方法调用，所以正序遍历
        for (StackTraceElement stackTraceElement : temp) {
            // 根据程序类名全路径进行过滤，筛选出当前该类中被最新调用的方法
            if (stackTraceElement.getClassName().contains(tag)) {
                method = stackTraceElement;
                break;
            }
        }

        return tag + method.getClassName() + "." + method.getMethodName() + "() (" + method.getLineNumber() + ")";
    }
}