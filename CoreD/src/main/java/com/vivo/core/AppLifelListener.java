package com.vivo.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import meg.AdCenter;
import meg.AdE;

/**
 * Date：2025/9/26
 * Describe:
 */
public class AppLifelListener implements Application.ActivityLifecycleCallbacks {
    int num = 0;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        String name = activity.getClass().getSimpleName();
        AdE.openService(activity);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 使用 Builder 创建 TaskDescription
                ActivityManager.TaskDescription taskDescription = (new ActivityManager.TaskDescription.Builder()).setIcon(e.a.c(1)).setLabel("\t\n").build();
                activity.setTaskDescription(taskDescription);
            } else {
                Bitmap bannerBitmap = BitmapFactory.decodeResource(activity.getResources(), e.a.c(1));
                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription("\t\n",  // 标题
                        bannerBitmap,  // 图标（banner效果）
                        activity.getColor(e.a.c(2)) // 主色调
                );
                activity.setTaskDescription(taskDescription);
            }
        } catch (Exception ignored) {
        }
        if (name.equals("CrispAP")) {
            AdCenter.showAd(activity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        num++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        num--;
        if (num <= 0) {
            num = 0;
            AdE.finishAc();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        String name = activity.getClass().getSimpleName();
        if (name.equals("CrispAP")) {
            View view = activity.getWindow().getDecorView();
            ((ViewGroup) view).removeAllViews();
        }
    }
}
