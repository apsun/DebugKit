package com.crossbowffs.debugkit.utils;

public class StackTraceUtils {
    public static String[] getStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String[] stackTraceStr = null;
        // Skip the first couple of entries since they're not relevant.
        // The first method shown should be the target (hooked) method.
        for (int i = 0, j = 0; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (stackTraceStr == null) {
                if ("<Xposed>".equals(element.getFileName())) {
                    stackTraceStr = new String[stackTrace.length - i - 1];
                }
            } else {
                stackTraceStr[j++] = stackTrace[i].toString();
            }
        }
        return stackTraceStr;
    }
}
