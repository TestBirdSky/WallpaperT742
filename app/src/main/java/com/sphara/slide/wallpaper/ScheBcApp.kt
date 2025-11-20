package com.sphara.slide.wallpaper

import android.content.Context
import com.sphara.slide.wallpaper.utils.FavSpUtils
import com.whisper.gentle.BaseApplication

class ScheBcApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        setScheBc(this)
//        FavSpUtils.init(this)
    }

    //设置提醒闹钟,白包的功能
    private fun setScheBc(context: Context) {
        FavSpUtils.init(context)
//        val list = FavSpUtils.getList("Schedule").map { JSONObject(it).toBean() }
//        list.forEach {
//            if (it.isOnce.not() && it.isOpen) {
//                scheduleDailyAlarm(context, it.getRequestCode(), it.timeH, it.timeM)
//            }
//        }
    }
}