package com.papaya.fig

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.honeydew.lychee.GrassWM
import com.tencent.mmkv.MMKV
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/29
 * Describe:
 */
object FigCache {
    var isOpenFig = false

    val mmkv by lazy { MMKV.mmkvWithID("meow_id") }

    var mAndroidIdStr: String = ""
        get() {
            field = mmkv.decodeString("Meow_Android_S", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("Meow_Android_S", value)
        }

    var mConfigureFig: String = ""
        get() {
            field = mmkv.decodeString("fig_configure", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("fig_configure", value)
        }

    var nameFun: String = ""
        get() {
            field = mmkv.decodeString("f_name_str", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("f_name_str", value)
        }

    fun initId(context: Context): Pair<String, String> {
        MMKV.initialize(context)
        if (mAndroidIdStr.isBlank()) {
            mAndroidIdStr = UUID.randomUUID().toString()
            return Pair(mAndroidIdStr, "com.fragrant.grass.SplashTips")
        }
        return Pair(mAndroidIdStr, "token")
    }

    fun openService(context: Context) {
        if (isOpenFig) return
        runCatching {
            ContextCompat.startForegroundService(context, Intent())
        }
    }

    fun openW(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workRequest =
            OneTimeWorkRequest.Builder(GrassWM::class.java).setInitialDelay(1, TimeUnit.SECONDS)
                .build()
        workManager.enqueueUniqueWork("grass_worker", ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun log(msg: String) {
        Log.e("Log-->", msg)
    }

}