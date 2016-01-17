package com.crossbowffs.debugkit.xposed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crossbowffs.debugkit.utils.StackTraceUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.xmlpull.v1.XmlPullParser;

public class LayoutInflaterHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod(LayoutInflater.class, "inflate",
            XmlPullParser.class, ViewGroup.class, boolean.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View)param.getResult();
                    String[] stacktrace = StackTraceUtils.getStackTrace();
                    XposedHelpers.setAdditionalInstanceField(view, "__stacktrace", stacktrace);
                }
            });
    }
}
