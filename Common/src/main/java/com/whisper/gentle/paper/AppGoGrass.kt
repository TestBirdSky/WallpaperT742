package com.whisper.gentle.paper

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.Gentle.Petal.GpW
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.whisper.gentle.FigCache
import com.whisper.gentle.FigSdkInit
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/29
 * Describe:
 */
class AppGoGrass {
    private val mFigSdkInit by lazy { FigSdkInit() }

    private fun opGo(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val work = PeriodicWorkRequest.Builder(GpW::class.java, 15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "custom_worker", ExistingPeriodicWorkPolicy.REPLACE, work
        )
    }

    fun checkProgress(context: Context, mainBlock: () -> Unit) {
        Firebase.initialize(context)
        opGo(context)
        val id = FigCache.initId(context)
        mFigSdkInit.figInitSdk(context, id)
        FigSdkInit().initSdk(context)
        mainBlock.invoke()
    }

//    private fun getProName(context: Context): String {
//        with(context) {
//            runCatching {
//                val am = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
//                val runningApps = am.runningAppProcesses ?: return ""
//                for (info in runningApps) {
//                    when (info.pid) {
//                        android.os.Process.myPid() -> return info.processName
//                    }
//                }
//            }
//        }
//        return ""
//    }
}