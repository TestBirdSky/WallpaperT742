package com.sphara.slide.wallpaper

import android.app.Application
import android.content.Context
import com.sphara.slide.wallpaper.data.ScheData.Companion.toBean
import com.sphara.slide.wallpaper.utils.AlarmUtils.scheduleDailyAlarm
import com.sphara.slide.wallpaper.utils.FavSpUtils
import org.json.JSONObject

class ScheBcApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setScheBc(this)
    }

    //设置提醒闹钟,白包的功能
    private fun setScheBc(context: Context) {
        FavSpUtils.init(context)
        val list = FavSpUtils.getList("Schedule").map { JSONObject(it).toBean() }
        list.forEach {
            if (it.isOnce.not() && it.isOpen) {
                scheduleDailyAlarm(context, it.getRequestCode(), it.timeH, it.timeM)
            }
        }
    }
}