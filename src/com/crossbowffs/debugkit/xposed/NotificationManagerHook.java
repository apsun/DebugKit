package com.crossbowffs.debugkit.xposed;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.UserHandle;
import com.crossbowffs.debugkit.utils.StackTraceUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NotificationManagerHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XC_MethodHook hook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Notification notification = (Notification)param.args[2];
                String[] stacktrace = StackTraceUtils.getStackTrace();
                notification.extras.putStringArray("__stacktrace", stacktrace);
                XposedBridge.log("---------- Notification Stacktrace Start ----------");
                for (String element : stacktrace) {
                    XposedBridge.log("> " + element);
                }
                XposedBridge.log("----------- Notification Stacktrace End -----------");
            }
        };

        XposedHelpers.findAndHookMethod(NotificationManager.class, "notify",
            String.class, int.class, Notification.class, hook);

        XposedHelpers.findAndHookMethod(NotificationManager.class, "notifyAsUser",
            String.class, int.class, Notification.class, UserHandle.class, hook);
    }
}
