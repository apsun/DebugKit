package com.crossbowffs.debugkit.xposed;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.view.View;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NotificationGutsHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!"com.android.systemui".equals(lpparam.packageName)) {
            return;
        }

        XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.BaseStatusBar", lpparam.classLoader,
            "bindGuts", "com.android.systemui.statusbar.ExpandableNotificationRow", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Context context = (Context)XposedHelpers.getObjectField(param.thisObject, "mContext");

                    StatusBarNotification sbn = (StatusBarNotification)XposedHelpers.callMethod(param.args[0], "getStatusBarNotification");
                    Notification notification = sbn.getNotification();
                    final String[] stacktrace = notification.extras.getStringArray("__stacktrace");

                    View guts = (View)XposedHelpers.callMethod(param.args[0], "getGuts");
                    int settingsButtonId = guts.getResources().getIdentifier("notification_inspect_item", "id", "com.android.systemui");
                    View settingsButton = guts.findViewById(settingsButtonId);
                    settingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("com.crossbowffs.debugkit.SHOW_NOTIFICATION_INFO");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("__stacktrace", stacktrace);
                            context.startActivity(intent);
                        }
                    });
                }
            });
    }
}
