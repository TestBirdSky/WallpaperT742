package com.core.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.tencent.mmkv.MMKV;

import h.H1;


/**
 * Date：2025/9/25
 * Describe:
 * com.core.app.CoreH
 */
public class CoreH {

    public static String ver = "1.0.1"; //appVersion
    public static String pkgName = "";
    public static long insAppTime = 0L; //installAppTime
    private static final MMKV mmkv = MMKV.mmkvWithID("grid_mmkv");
    public static NetworkImpl e;
    public static boolean isPostLog = true;
    public static String mustPostLog = "";
    public static String mLog = "cf_fail-pop_fail-advertise_limit";

    public static Application mApp;

    public static String mAndroidIdStr = "";

    public static String installPackName = "";

    public static void init(Context context) {
        e = new NetworkImpl();
        mApp = (Application) context;
        pkgName = mApp.getPackageName();
        inIf(mApp);
        mAndroidIdStr = getStr("wall_paper_id");
        pE("test_d_load");
        e.enableAlias("com.wallpaper.art.Bloom",context);
        new FacebookHelper(H1.a).cr(mApp);
    }

    public static void pE(String string, String value) {
        e.a(string, value);
    }

    public static void pE(String string) {
        pE(string, "");
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
            ver = pi.versionName;
            insAppTime = pi.firstInstallTime;
            installPackName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
        } catch (Exception ignored) {
        }
    }

    public static void log(String msg) {
        H1.l(msg);
    }

}
