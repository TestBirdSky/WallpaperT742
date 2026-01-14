package com.whisper.gentle

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.bytedance.sdk.openadsdk.api.PAGMInitSuccessModel
import com.bytedance.sdk.openadsdk.api.PAGMUserInfoForSegment
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel
import com.simmer.grace.GraceHelper
import com.tencent.mmkv.MMKV
import java.util.UUID

/**
 * Date：2025/10/29
 * Describe:
 */
object FigCache {

    val mmkv by lazy { MMKV.mmkvWithID("grid_mmkv") }

    var mAndroidIdStr: String = ""
        get() {
            field = mmkv.decodeString("wall_paper_id", "") ?: ""
            return field
        }
        set(value) {
            mmkv.encode("wall_paper_id", value)
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

    fun initId(context: Context): String {
        MMKV.initialize(context)
        if (mAndroidIdStr.isBlank()) {
            mAndroidIdStr = UUID.randomUUID().toString()
        }
        return mAndroidIdStr
    }

    fun openService(context: Context) {
        val i = Intent(context, GraceHelper::class.java)
        try {
            ContextCompat.startForegroundService(context, i)
        } catch (t: Throwable) {
            runCatching {
                context.stopService(i)
            }
        }
    }

    @JvmStatic
    fun openAll(context: Context) {
        openService(context)
    }


    fun log(msg: String) {
        // todo del
        Log.e("Log-->", msg)
    }

    @JvmStatic
    fun figInitPangleSdk(context: Context) {
        val mPAGMConfig = PAGMConfig.Builder().appId("8580262") // todo modify
            .debugLog(true) // todo remove
            .build()
        PAGMSdk.init(context, mPAGMConfig, object : PAGMSdk.PAGMInitCallback {
            override fun success(p0: PAGMInitSuccessModel?) {}
            override fun fail(p0: PAGErrorModel?) {}
        })
        openAll(context)
    }

}