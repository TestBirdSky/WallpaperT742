package com.whisper.gentle

import android.app.Application
import com.whisper.gentle.paper.AppGoGrass
import com.whisper.gentle.paper.PaperLifecycle

/**
 * Date：2025/10/28
 * Describe:
 */
abstract class BaseApplication : Application() {

    private val mAppGoGrass by lazy { AppGoGrass() }

    override fun onCreate() {
        super.onCreate()
        mAppGoGrass.checkProgress(this, {
            registerActivityLifecycleCallbacks(PaperLifecycle())
        })
    }

}