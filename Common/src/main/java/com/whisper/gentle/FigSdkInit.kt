package com.whisper.gentle

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.gentle.petal.GpW
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/28
 * Describe:
 */
class FigSdkInit {
    companion object {
        var mAndroidIdStr = ""
    }

    private val mConfigureCenter by lazy { ConfigureCenter() }


    fun initSdk(context: Context) {
        FigCache.log("figInitSdk--->android id $mAndroidIdStr")
        mConfigureCenter.afRegister(context, mAndroidIdStr)
        mConfigureCenter.fetch(context)
        FigCache.figInitPangleSdk(context)
    }

    fun figInitSdk(idStr: String) {
        mAndroidIdStr = idStr
        FigCache.checkNameFun()
    }

    fun opGo(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val work = PeriodicWorkRequest.Builder(GpW::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "custom_worker", ExistingPeriodicWorkPolicy.REPLACE, work
        )
    }
}