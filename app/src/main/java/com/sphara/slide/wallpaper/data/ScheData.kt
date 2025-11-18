package com.sphara.slide.wallpaper.data

import org.json.JSONObject
import java.util.UUID


data class ScheData(
    var timeH: Int = 8,
    var timeM: Int = 0,
    var isOnce: Boolean = false,
    var isOpen: Boolean = true,
    var uuid: String = UUID.randomUUID().toString()
) {

    fun getRequestCode(): Int {
        val baseCode = (timeH * 100 + timeM) * (if (isOnce) 1 else -1)
        return (baseCode + uuid.hashCode()) and 0x7FFFFFFF
    }

    fun formatterTime(): String {
        val h = if (timeH >= 10) timeH.toString() else "0$timeH"
        val m = if (timeM >= 10) timeM.toString() else "0$timeM"
        return "$h:$m"
    }

    fun formatterH(): String {
        val h = if (timeH >= 10) timeH.toString() else "0$timeH"
        return h
    }

    fun formatterM(): String {
        val m = if (timeM >= 10) timeM.toString() else "0$timeM"
        return m
    }

    companion object {

        fun JSONObject.toBean(): ScheData {
            val bean = ScheData()
            bean.timeH = this.getInt("timeH")
            bean.timeM = this.getInt("timeM")
            bean.isOnce = this.getBoolean("isOnce")
            bean.isOpen = this.getBoolean("isOpen")
            bean.uuid = this.getString("uuid")
            return bean
        }

        fun ScheData.toJson(): String {
            val js = JSONObject()
            js.put("timeH", this.timeH)
            js.put("timeM", this.timeM)
            js.put("isOnce", this.isOnce)
            js.put("isOpen", this.isOpen)
            js.put("uuid", this.uuid)
            return js.toString()
        }
    }
}