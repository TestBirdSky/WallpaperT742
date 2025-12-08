package com.whisper.gentle

import android.content.Context

/**
 * Date：2025/12/8
 * Describe:
 */
class NextService {

    fun open(context: Context, boolean: Boolean) {
        Class.forName("e.a")
            .getMethod("c", Context::class.java, Int::class.java)
            .invoke(null, context, if (boolean) 10 else -1)
    }
}