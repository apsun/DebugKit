package com.crossbowffs.debugkit.utils;

public class StackTraceUtils {
    public static String[] getStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String[] stackTraceStr = new String[stackTrace.length];
        for (int i = 0; i < stackTrace.length; ++i) {
            stackTraceStr[i] = stackTrace[i].toString();
        }
        return stackTraceStr;
    }
}
