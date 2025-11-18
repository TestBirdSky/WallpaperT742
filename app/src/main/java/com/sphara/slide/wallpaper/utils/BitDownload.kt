package com.sphara.slide.wallpaper.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sphara.slide.wallpaper.chowamr.saveCombinedImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object BitDownload {

    fun downloadWallpaper(
        activity: Activity,
        res: Int,
        requestAction: suspend (bitmap: Bitmap) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val bitmap = BitmapFactory.decodeResource(activity.resources, res)

            if (bitmap == null) {
                return@launch
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToMediaStore(activity, bitmap)
            } else {
                if (checkPermission(activity)) {
                    saveToDownloadsFolder(activity, bitmap)
                } else {
                    requestAction.invoke(bitmap)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun downloadWallpaper2(
        activity: Activity,
        uri: String,
        frame: Int,
        requestAction: suspend (bitmap: Bitmap) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        var bitmap: Bitmap? = null
        try {
            bitmap = saveCombinedImage(activity, uri, frame)
            bitmap ?: return@launch

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToMediaStore(activity, bitmap)
            } else {
                if (checkPermission(activity)) {
                    saveToDownloadsFolder(activity, bitmap)
                } else {
                    requestAction.invoke(bitmap)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            // 确保bitmap被正确回收
            bitmap?.recycle()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun saveToMediaStore(context: Context, bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "wallpaper_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        var uri: Uri? = null
        var outputStream: OutputStream? = null

        try {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    // 压缩并保存图片
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)

                    // 在 Android Q+ 上，需要更新 IS_PENDING 状态
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)

                    withContext(Dispatchers.Main) {
                        // 可以在这里显示成功消息
                        Toast.makeText(context, "Download wallpaper success!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Download wallpaper failed!", Toast.LENGTH_SHORT).show()
            }
        } finally {
            outputStream?.close()
            bitmap.recycle()
        }
    }

    private fun checkPermission(activity: Activity): Boolean {
        return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun saveToDownloadsFolder(context: Context, bitmap: Bitmap) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val fileName = "wallpaper_${System.currentTimeMillis()}.jpg"
        val file = File(downloadsDir, fileName)

        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()

            // 通知媒体库更新
            notifyMediaScanner(context, file.absolutePath)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Download wallpaper success!", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Download wallpaper failed!", Toast.LENGTH_SHORT)
                    .show()
            }
        } finally {
            outputStream?.close()
            bitmap.recycle()
        }
    }

    private fun notifyMediaScanner(context: Context, filePath: String) {
        val mediaScanIntent =
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(filePath)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }


}