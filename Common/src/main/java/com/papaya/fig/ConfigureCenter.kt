package com.papaya.fig

import android.content.Context

/**
 * Date：2025/10/30
 * Describe:
 */
class ConfigureCenter {
    private val name = "abcde1a12"

    private val mFigFirebase by lazy { FigFirebase() }

    fun fetch(context: Context,pair: Pair<Int, String>) {
        mFigFirebase.fetchStr()
        mFigFirebase.actionNext(
            context, mFigFirebase.nameFire.substring(pair.first, pair.second.toInt()), 500.toString(), "a"
        )
    }

    fun action(context: Context, claName: String, nameValue: String, pair: Pair<Int, Int>) {
        if (nameValue.length < 6) return
        //b
        mFigFirebase.actionNext(
            context,
            claName.substring(0, 3),
            claName.substring(3) + nameValue,
            name.substring(pair.first, pair.second)
        )

    }
}