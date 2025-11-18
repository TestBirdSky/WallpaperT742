package com.fragrant.grass.orchids

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fragrant.grass.orchids.OrchidsApp.Companion.mInstance
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.papaya.fig.FigSdkInit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/29
 * Describe:
 */
class AppGoGrass {
    private fun opGo(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val work = PeriodicWorkRequest.Builder(OrchidsWo::class.java, 15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "core_worker", ExistingPeriodicWorkPolicy.REPLACE, work
        )
    }

    fun checkProgress(context: Context, mainBlock: () -> Unit) {
        if (context.packageName == getProName(context)) {
            Firebase.initialize(mInstance)
            FigSdkInit().initSdk(context)
            mainBlock.invoke()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val processName: String = Application.getProcessName()
                if (processName.isNotBlank()) {
                    WebView.setDataDirectorySuffix(processName)
                }
            }
        }
        opGo(mInstance)
    }

    private fun getProName(context: Context): String {
        with(context) {
            runCatching {
                val am = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
                val runningApps = am.runningAppProcesses ?: return ""
                for (info in runningApps) {
                    when (info.pid) {
                        android.os.Process.myPid() -> return info.processName
                    }
                }
            }
        }
        return ""
    }
}