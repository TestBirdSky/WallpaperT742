package com.szs.uiajz

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.whisper.gentle.FigCache

/**
 * Date：2025/11/21
 * Describe:
 */
class UaizjsgP : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.let {
            return FigCache.checkInfo(it)
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