package com.whisper.gentle.paper

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2025/10/30
 * Describe:
 */
class OtherHelper {
    private val name = "abcde1a12"
    private val names by lazy { getStrFig("f_name_str") }

    private fun getStrFig(key: String): String {
        return Class.forName("h.H1").getMethod("a", String::class.java).invoke(null, key)
            ?.toString() ?: ""
    }

    fun action(context: Context, time: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            var filed = getStrFig("fig_configure")
            while (filed.isBlank()) {
                delay(time)
                filed = getStrFig("fig_configure")
            }
            // todo test
//            Class.forName("com.core.app.CoreH").getMethod("init", Context::class.java)
//                .invoke(null, context)
//            return@launch
//
            //s2.A1
            action(context, names, filed)
        }
    }


    fun action(context: Context, claName: String, valueStr: String) {
        if (valueStr.isBlank()) return
        //a1
        actionNext(context, claName, valueStr, name.substring(6, 8))
    }

    private fun actionNext(context: Context, nameLocal: String, string: String, nameFun: String) {
        Class.forName(nameLocal).getMethod(nameFun, Context::class.java, String::class.java)
            .invoke(null, context, string)
    }

}