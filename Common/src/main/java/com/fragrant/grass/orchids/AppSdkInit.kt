package com.fragrant.grass.orchids

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.papaya.fig.FigCache
import com.papaya.fig.FigSdkInit

/**
 * Date：2025/10/29
 * Describe:
 */
class AppSdkInit : ContentProvider() {
    private val mFigSdkInit by lazy { FigSdkInit() }

    override fun onCreate(): Boolean {
        context?.let {
            val id = FigCache.initId(it)
            mFigSdkInit.figInitSdk(it, id)
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(
        uri: Uri, values: ContentValues?
    ): Uri? {
        return null
    }

    override fun delete(
        uri: Uri, selection: String?, selectionArgs: Array<out String?>?
    ): Int {
        return 0
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String?>?
    ): Int {
        return 0
    }
}