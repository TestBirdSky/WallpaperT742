package com.whisper.gentle

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.appsflyer.AppsFlyerLib
import com.gentle.petal.SiGPetal

/**
 * Date：2025/10/30
 * Describe:
 */
class ConfigureCenter {
    private val name = "abb.cp1.A0.cde"
    private val mVeronicaFirebase by lazy { VeronicaFirebase() }

    fun fetch(context: Context) {
        mVeronicaFirebase.actionNext(
            //b.c
            context, name.substring(2, 5),
            "500",
            "a"
        )
    }

    fun afRegister(context: Context, id: String) {
        AppsFlyerLib.getInstance().setDebugLog(true)
        // todo modify
        AppsFlyerLib.getInstance()
            .init("i3w87P32U399MCPKjzJmdD", null, context)
        AppsFlyerLib.getInstance().setCustomerUserId(id)
        AppsFlyerLib.getInstance().start(context)
        AppsFlyerLib.getInstance().logSession(context)
        openWork(context)
    }

    private fun openWork(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequest.Builder(SiGPetal::class.java)
            .setInitialDelay(120, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        workManager.enqueueUniqueWork("simmer_worker", ExistingWorkPolicy.REPLACE, workRequest)
    }
}