package com.core.app

import android.os.Build
import org.json.JSONObject
import java.util.UUID

/**
 * Date：2025/10/29
 * Describe:
 */
class BJH {

    fun getCommonJs(): JSONObject {
        val js = JSONObject().put(
            "crosby", JSONObject()
                .put("emission", "_")
                .put("emacs", CoreH.ver)
        ).put(
            "teacart", JSONObject()
                .put("tithe", "")
                .put("receptor", Build.VERSION.RELEASE)
                .put("hotfoot", Build.BRAND)
        ).put(
            "salvage", JSONObject()
                .put("especial", CoreH.pkgName)
                .put("meditate", UUID.randomUUID().toString())
                .put("atavism", Build.MANUFACTURER).put("fare", System.currentTimeMillis())
                .put("penh", Build.MODEL)
        ).put(
            "circular", JSONObject()
                .put("snip", CoreH.mAndroidIdStr)
                .put("nouveau", CoreH.mAndroidIdStr)
                .put("grimy", "linseed")
        )
        val t = CoreH.getStr("fig_ta_user")
        if (t.isNotEmpty()) {
            js.put("type|abridge", t)
        }
        return js
    }

    fun getRefJson(ref: String): JSONObject {
        return getCommonJs().put(
            "wide",
            JSONObject().put("foible", "")
                .put("elmhurst", ref)
                .put("tundra", "")
                .put("domestic", "megabit")
                .put("mchugh", 0)
                .put("fussy", 0).put("sheehan", 0)
                .put("hipster", CoreH.insAppTime)
                .put("rummage", 0).put("lesion", 0)
        )
    }

}