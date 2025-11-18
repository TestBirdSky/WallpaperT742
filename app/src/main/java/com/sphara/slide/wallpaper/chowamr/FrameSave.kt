package com.sphara.slide.wallpaper.chowamr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 保存叠加图片到本地（按原始尺寸和UI位置比例）
 */
suspend fun saveCombinedImage(
    context: Context,
    uri: String,
    frame: Int,
): Bitmap? {
    var bitmap: Bitmap? = null
    var uriBitmap: Bitmap? = null
    var frameBitmap: Bitmap? = null
    var scaledUriBitmap: Bitmap? = null

    try {
        // 加载URI图片（原始尺寸）
        uriBitmap = loadBitmapFromUri(context, uri)
        // 加载frame图片（原始尺寸）
        frameBitmap = loadBitmapFromResource(context, frame)

        if (uriBitmap != null && frameBitmap != null) {
            // 使用frame图片的原始尺寸作为最终图片尺寸
            val frameWidth = frameBitmap.width
            val frameHeight = frameBitmap.height

            // 计算URI图片在frame中的位置比例（基于UI布局）
            val uriScale = 250f / 300f // URI图片相对于frame的比例

            // 计算URI图片在最终图片中的尺寸（保持宽高比）
            val uriTargetWidth = (frameWidth * uriScale).toInt()
            val uriTargetHeight =
                (uriBitmap.height * uriTargetWidth / uriBitmap.width).toInt() // 保持宽高比

            // 计算URI图片在frame中的位置（居中）
            val uriLeft = (frameWidth - uriTargetWidth) / 2
            val uriTop = (frameHeight - uriTargetHeight) / 2

            // 创建叠加的Bitmap（使用frame的原始尺寸）
            val combinedBitmap = createBitmap(frameWidth, frameHeight)
            val canvas = Canvas(combinedBitmap)

            // 绘制背景透明
            canvas.drawColor(Color.TRANSPARENT)

            // 缩放URI图片到目标尺寸（保持宽高比）
            scaledUriBitmap = uriBitmap.scale(uriTargetWidth, uriTargetHeight)

            // 先绘制URI图片（在计算的位置）
            canvas.drawBitmap(scaledUriBitmap, uriLeft.toFloat(), uriTop.toFloat(), null)
            // 再绘制frame图片（覆盖在上面，原始尺寸）
            canvas.drawBitmap(frameBitmap, 0f, 0f, null)

            bitmap = combinedBitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    } finally {
        // 只回收临时资源，不回收要返回的bitmap
        scaledUriBitmap?.recycle()
        uriBitmap?.recycle()
        frameBitmap?.recycle()
    }
    return bitmap
}


/**
 * 从URI加载Bitmap
 */
private suspend fun loadBitmapFromUri(context: android.content.Context, uri: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri.toUri())
            inputStream?.use { stream ->
                android.graphics.BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

/**
 * 从资源加载Bitmap
 */
private fun loadBitmapFromResource(context: android.content.Context, resId: Int): Bitmap? {
    return try {
        val options = android.graphics.BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        android.graphics.BitmapFactory.decodeResource(context.resources, resId, options)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

