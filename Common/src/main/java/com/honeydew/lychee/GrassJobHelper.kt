package com.honeydew.lychee

import android.annotation.SuppressLint
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent

/**
 * Date：2025/10/30
 * Describe:
 */
@SuppressLint("SpecifyJobSchedulerIdRange")
class GrassJobHelper : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }
}