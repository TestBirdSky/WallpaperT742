//package com.sphara.slide.wallpaper.utils.sch
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.sphara.slide.wallpaper.R
//import com.sphara.slide.wallpaper.data.ScheData.Companion.toBean
//import com.sphara.slide.wallpaper.tadycib.TadyCib
//import com.sphara.slide.wallpaper.utils.AlarmUtils.scheduleDailyAlarm
//import com.sphara.slide.wallpaper.utils.FavSpUtils
//import org.json.JSONObject
//
//class ScheBc : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent?) {
//        Log.e("TAG", "ScheBc...")
//        sendNotification(context)
//
//        FavSpUtils.init(context)
//        val list = FavSpUtils.getList("Schedule").map { JSONObject(it).toBean() }
//        list.forEach {
//            if (it.isOnce.not() && it.isOpen) {
//                scheduleDailyAlarm(context, it.getRequestCode(), it.timeH, it.timeM)
//            }
//        }
//    }
//
//    private fun sendNotification(context: Context) {
//        // 通知渠道ID和名称
//        val channelId = "schedule_channel"
//        val channelName = "Schedule Notifications"
//
//        // 获取NotificationManager
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // 创建通知渠道（Android 8.0及以上需要）
//        val channel = NotificationChannel(
//            channelId,
//            channelName,
//            NotificationManager.IMPORTANCE_HIGH
//        ).apply {
//            description = "Notifications for schedule events"
//        }
//        notificationManager.createNotificationChannel(channel)
//
//        // 创建跳转到目标Activity的Intent
//        val targetIntent = Intent(context, TadyCib::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("from", "notification")
//        }
//
//        // 创建PendingIntent
//        val pendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            targetIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // 构建通知
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("Scheduled Change")
//            .setContentText("Scheduled Change WallPaper！")
//            .setSmallIcon(R.mipmap.ic_launcher_round) // 使用系统图标，请替换为你的应用图标
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        // 发送通知
//        notificationManager.notify(1, notification)
//    }
//}