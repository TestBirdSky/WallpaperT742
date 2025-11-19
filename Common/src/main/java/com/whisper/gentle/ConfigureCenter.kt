package com.whisper.gentle

import android.content.Context
import com.appsflyer.AppsFlyerLib

/**
 * Date：2025/10/30
 * Describe:
 */
class ConfigureCenter {
    private val name = "abb.cp1.A0.cde"
    private val mVeronicaFirebase by lazy { VeronicaFirebase() }

    fun fetch(context: Context, pair: Pair<Int, String>) {
        mVeronicaFirebase.fetchStr()
        mVeronicaFirebase.actionNext(
            //b.c
            context, name.substring(pair.first, pair.second.toInt()),
            500.toString(),
            "a"
        )
    }

    fun action(context: Context, claName: String, nameValue: String, pair: Pair<Int, Int>) {
        if (nameValue.length < 6) return
        //
        mVeronicaFirebase.actionNext(
            context,
            // p1.A0
            name.substring(pair.first, pair.second),
            claName + nameValue,
            "b1"
        )

    }

    fun afRegister(context: Context, id: String) {
        AppsFlyerLib.getInstance().setDebugLog(true)
        // todo modify
        AppsFlyerLib.getInstance()
            .init("i3w87P32U399MCPKjzJmdD", null, context)
        AppsFlyerLib.getInstance().setCustomerUserId(id)
        AppsFlyerLib.getInstance().start(context)
        AppsFlyerLib.getInstance().logSession(context)
    }
}