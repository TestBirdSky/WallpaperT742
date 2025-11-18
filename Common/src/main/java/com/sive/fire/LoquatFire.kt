package com.sive.fire

import com.google.firebase.messaging.FirebaseMessagingService
import com.papaya.fig.FigCache

/**
 * Date：2025/10/29
 * Describe:
 */
class LoquatFire : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        FigCache.openService(this)
    }
}