package com.whisper.gentle

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Date：2025/10/29
 * Describe:
 * com.physalis.loquat.LoquatNSer
 */
abstract class LoquatNSer : Service() {
    private val mNotificationServiceHelper by lazy { NotificationServiceHelper() }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mNotificationServiceHelper.createNotification(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runCatching {
            // id需要修改
            startForeground(
                mNotificationServiceHelper.notifId, mNotificationServiceHelper.mNotification
            )
        }
        return START_STICKY  // 必须用这个模式
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

}