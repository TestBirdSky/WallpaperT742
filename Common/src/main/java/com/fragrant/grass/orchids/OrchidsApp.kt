package com.fragrant.grass.orchids

import android.app.Activity
import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

/**
 * Date：2025/10/28
 * Describe:
 */
class OrchidsApp : Application() {
    companion object {
        @JvmStatic
        lateinit var mInstance: Application

    }

    private val mAppGoGrass by lazy { AppGoGrass() }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mAppGoGrass.checkProgress(this, {
            registerActivityLifecycleCallbacks(GrassLifecycle())
        })
    }

}