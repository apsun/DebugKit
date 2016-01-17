package com.crossbowffs.debugkit.xposed;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.Arrays;

public class ViewAccessibilityHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod(View.class, "onInitializeAccessibilityNodeInfo",
            AccessibilityNodeInfo.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    AccessibilityNodeInfo info = ((AccessibilityNodeInfo)param.args[0]);
                    info.setClassName(param.thisObject.getClass().getName());
                    String[] stacktrace = (String[])XposedHelpers.getAdditionalInstanceField(param.thisObject, "__stacktrace");
                    CharSequence oldContentDescription = info.getContentDescription();
                    info.setContentDescription(Arrays.toString(stacktrace));
                }
            });
    }
}
