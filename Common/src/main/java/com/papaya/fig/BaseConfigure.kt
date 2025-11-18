package com.papaya.fig

import android.content.Context
import java.lang.reflect.Method

/**
 * Date：2025/10/30
 * Describe:
 */
abstract class BaseConfigure {
    var str = ""
    val nameInfo by lazy { "tools.Helper" }

    fun actionNext(context: Context, claName: String, value: String, nameFun: String) {
        action(claName, nameFun)?.invoke(null, context, value)
    }

    //com.applovin.impl.tools.Helper


    private fun action(claName: String, nameFun: String): Method? {
        runCatching {
            return Class.forName(claName)
                .getMethod(nameFun, Context::class.java, String::class.java)
        }.onFailure {
            it.printStackTrace()
        }
        return null
    }

}