package com.google.open.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.whisper.gentle.NextService

/**
 * Date：2025/12/8
 * Describe:
 * com.google.open.service.FirebaseService
 */
class FirebaseService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        NextService().open(this, false)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return 1
    }
}