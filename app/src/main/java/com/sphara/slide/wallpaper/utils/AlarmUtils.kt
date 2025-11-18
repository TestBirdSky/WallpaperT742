package com.sphara.slide.wallpaper.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sphara.slide.wallpaper.utils.sch.ScheBc
import java.util.Calendar


object AlarmUtils {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleDailyAlarm(
        context: Context,
        requestCode: Int,
        hourOfDay: Int,
        minute: Int,
    ) {
        if (canScheduleExactAlarms(context).not()) {
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ScheBc::class.java).apply {
            action = "MY_DAILY_ALARM_ACTION"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 设置触发时间
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // 如果当前时间已经过了今天设定的时间，就设置为明天
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        // 根据版本使用不同的设置方法
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

}