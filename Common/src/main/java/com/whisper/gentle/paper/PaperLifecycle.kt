package com.whisper.gentle.paper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.whisper.gentle.ActivityListHelper
import com.whisper.gentle.FigCache

/**
 * Date：2025/10/29
 * Describe:
 */
class PaperLifecycle : Application.ActivityLifecycleCallbacks {
    private var time = 0L

    override fun onActivityCreated(
        activity: Activity, savedInstanceState: Bundle?
    ) {
        ActivityListHelper.addAc(activity)
        if (System.currentTimeMillis() - time < 60000 * 60) {
            time = System.currentTimeMillis()
            FigCache.openService(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(
        activity: Activity, outState: Bundle
    ) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        ActivityListHelper.removeAc(activity)
    }
}