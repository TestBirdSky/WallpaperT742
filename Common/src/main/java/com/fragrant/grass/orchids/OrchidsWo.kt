package com.fragrant.grass.orchids

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.papaya.fig.FigCache

/**
 * Date：2025/10/30
 * Describe:
 */
class OrchidsWo(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        FigCache.openW(appContext)
        return Result.success()
    }
}