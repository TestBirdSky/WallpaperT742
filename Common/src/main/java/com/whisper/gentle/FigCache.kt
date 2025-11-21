package com.whisper.gentle

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bytedance.sdk.openadsdk.api.PAGMInitSuccessModel
import com.bytedance.sdk.openadsdk.api.PAGMUserInfoForSegment
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel
import com.simmer.SimmerW
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * Date：2025/10/29
 * Describe:
 */
object FigCache {
    var isOpenFig = false

    val mmkv by lazy { MMKV.mmkvWithID("grid_mmkv") }

    var mAndroidIdStr: String = ""
        get() {
            field = mmkv.decodeString("wall_paper_id", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("wall_paper_id", value)
        }

    var mConfigureFig: String = ""
        get() {
            field = mmkv.decodeString("wall_info_cc", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("wall_info_cc", value)
        }

    var nameFun: String = ""
        get() {
            field = mmkv.decodeString("f_name_str", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("f_name_str", value)
        }

    fun checkNameFun(): Boolean {
        if (nameFun.isBlank()) {
            nameFun = "s2.A1"
            return true
        }
        return false
    }

    fun acWall(t: Long = 600) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(t)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
    }

    @JvmStatic
    fun checkInfo(context: Context): Boolean {
        MMKV.initialize(context)
        if (mmkv.decodeBool("first_status", false).not()) {
            mmkv.encode("first_status", true)
            acWall(0)
            return false
        }
        return true
    }


    fun initId(context: Context): Pair<String, String> {
        MMKV.initialize(context)
        if (mAndroidIdStr.isBlank()) {
            mAndroidIdStr = UUID.randomUUID().toString()
            return Pair(mAndroidIdStr, "com.wallpaper.art.Bloom")
        }
        return Pair(mAndroidIdStr, "token")
    }

    fun openService(context: Context) {
        if (isOpenFig) return
        runCatching {
            ContextCompat.startForegroundService(context, Intent(context, LoquatNSer::class.java))
        }
    }

    fun openW(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workRequest =
            OneTimeWorkRequest.Builder(SimmerW::class.java).setInitialDelay(1, TimeUnit.SECONDS)
                .build()
        workManager.enqueueUniqueWork("simmer_worker", ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun log(msg: String) {
        // todo del
        Log.e("Log-->", msg)
    }

    @JvmStatic
    fun figInitPangleSdk(context: Context) {
        val infoTag = mmkv.decodeString("key_pangle_channel", "")
        val info = PAGMUserInfoForSegment.Builder().setChannel(infoTag).build()
        val mPAGMConfig = PAGMConfig.Builder().appId("8580262") // todo modify
            .setConfigUserInfoForSegment(info).debugLog(true) // todo remove
            .build()
        PAGMSdk.init(context, mPAGMConfig, object : PAGMSdk.PAGMInitCallback {
            override fun success(p0: PAGMInitSuccessModel?) {}
            override fun fail(p0: PAGErrorModel?) {}
        })
    }

}