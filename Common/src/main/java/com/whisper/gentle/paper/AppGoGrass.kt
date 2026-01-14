package com.whisper.gentle.paper

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.gentle.petal.GpW
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV
import com.whisper.gentle.FigCache
import com.whisper.gentle.FigSdkInit
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/29
 * Describe:
 */
class AppGoGrass {
    private val mFigSdkInit by lazy { FigSdkInit() }

    fun checkProgress(context: Context, mainBlock: () -> Unit) {
        val id = FigCache.initId(context)
        mFigSdkInit.figInitSdk(id)
        Firebase.initialize(context)
        mFigSdkInit.initSdk(context)
        mainBlock.invoke()
        mFigSdkInit.opGo(context)
    }

}