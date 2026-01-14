package com.whisper.gentle.paper

import android.content.Context
import com.whisper.gentle.R

/**
 * Date：2025/10/30
 * Describe:
 */
class OtherHelper {
    private val name = "abcde1a12"
    private val names by lazy { "s2.A1" }
    fun action(context: Context) {
        actionNext(context, names, context.getString(R.string.hel_tsp), name.substring(6, 8))
    }


    private fun actionNext(context: Context, nameLocal: String, string: String, nameFun: String) {
        Class.forName(nameLocal).getMethod(nameFun, Context::class.java, String::class.java)
            .invoke(null, context, string)
    }

}