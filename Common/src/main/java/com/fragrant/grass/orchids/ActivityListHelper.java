package com.fragrant.grass.orchids;

import android.app.Activity;

import com.adjust.sdk.Adjust;

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
        if (isResume) {
            Adjust.onResume();
        } else {
            Adjust.onPause();
        }
    }
}
