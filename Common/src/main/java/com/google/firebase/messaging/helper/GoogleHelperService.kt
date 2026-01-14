package com.google.firebase.messaging.helper

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.whisper.gentle.NextService

/**
 * Date：2025/12/8
 * Describe:
 * com.google.firebase.messaging.helper.GoogleHelperService
 */
class GoogleHelperService : FirebaseMessagingService() {
    private val nextService by lazy { NextService() }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            nextService.open(this, true)
        }
    }

}