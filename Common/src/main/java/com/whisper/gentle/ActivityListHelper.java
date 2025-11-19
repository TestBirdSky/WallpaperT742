package com.whisper.gentle;

import android.app.Activity;


import java.util.ArrayList;

/**
 * Date：2025/10/31
 * Describe:
 */
public class ActivityListHelper {
    private static ArrayList<Activity> listAc = new ArrayList<>();

    public static ArrayList<Activity> getListAc() {
        return listAc;
    }

    public static void addAc(Activity activity) {
        listAc.add(activity);
    }


    public static void removeAc(Activity activity) {
        listAc.remove(activity);
    }

    public static void status(Boolean isResume) {

    }
}
