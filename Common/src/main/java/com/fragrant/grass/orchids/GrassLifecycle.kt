package com.fragrant.grass.orchids

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.papaya.fig.FigCache

/**
 * Date：2025/10/29
 * Describe:
 */
class GrassLifecycle : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(
        activity: Activity, savedInstanceState: Bundle?
    ) {
        ActivityListHelper.addAc(activity)
        FigCache.openService(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        ActivityListHelper.status(true)
    }

    override fun onActivityPaused(activity: Activity) {
        ActivityListHelper.status(false)
    }

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(
        activity: Activity, outState: Bundle
    ) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        ActivityListHelper.removeAc(activity)
    }
}