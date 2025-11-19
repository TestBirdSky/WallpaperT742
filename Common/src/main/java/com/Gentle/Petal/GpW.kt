package com.Gentle.Petal

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.whisper.gentle.FigCache

/**
 * Date：2025/11/18
 * Describe:
 */
class GpW(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        FigCache.openW(appContext)
        return Result.success()
    }
}