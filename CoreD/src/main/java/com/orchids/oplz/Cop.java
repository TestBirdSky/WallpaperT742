package com.orchids.oplz;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.facebook.login.LoginEvent;
import com.tencent.mmkv.MMKV;

import meg.GoStartMe;

/**
 * Date：2025/9/25
 * Describe:
 * com.orchids.oplz.Cop
 */
public class Cop {
    public static long insAppTime = 0L; //installAppTime
    private static final MMKV mmkv = MMKV.mmkvWithID("meow_id");
    public static LoginEvent e;
    public static Application mApp;

    public static void a(Context ctx, LoginEvent c) {
        e = c;
        mApp = (Application) ctx;
        pE("test_d_load2");
        inIf(mApp);
        GoStartMe.a2();
    }

    public static void pE(String string, String value) {
        e.a(string, value);
    }

    public static void pE(String string) {
        pE(string, "");
    }

    public static void postAd(String string) {
        e.b(string);
    }


    public static String getStr(String key) {
        return mmkv.decodeString(key, "");
    }

    public static void saveC(String ke, String con) {
        mmkv.encode(ke, con);
    }

    public static int getInt(String key) {
        return mmkv.decodeInt(key, 0);
    }

    public static void saveInt(String key, int i) {
        mmkv.encode(key, i);
    }

    private static void inIf(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            insAppTime = pi.firstInstallTime;
        } catch (Exception ignored) {
        }
    }
}
