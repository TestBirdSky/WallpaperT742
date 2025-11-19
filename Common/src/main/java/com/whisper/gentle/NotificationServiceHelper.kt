package com.whisper.gentle

import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service

/**
 * Date：2025/10/29
 * Describe:
 */
class NotificationServiceHelper {
    val notifId = 8901
    private val listName = arrayListOf("e.a", "MeowId29", "MeowChannel")
    var mNotification: Notification? = null

    fun createNotification(context: Context) {
        createChannel(context)
        mNotification =
            Class.forName(listName[0]).getMethod("f", Context::class.java, String::class.java)
                .invoke(null, context, listName[1]) as Notification?
    }

    private fun createChannel(context: Context) {
        val channel =
            NotificationChannel(listName[1], listName[2], NotificationManager.IMPORTANCE_DEFAULT)
        (context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
    }

}