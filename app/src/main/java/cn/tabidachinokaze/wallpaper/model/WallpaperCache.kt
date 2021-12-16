package cn.tabidachinokaze.wallpaper.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.LruCache
import androidx.annotation.WorkerThread
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import java.io.File
import java.io.InputStream

class WallpaperCache(private val context: Context) {
    private val bitmapCache: LruCache<String, Bitmap> = LruCache(1000)
    private val filesDir: File = context.filesDir
    private val cacheDir: File = context.cacheDir
    private val imageCacheDir: String = "image_cache"

    fun put(imageItem: ImageItem, value: Bitmap) {
        bitmapCache.put(imageItem.id, value)
        saveBitmap(imageItem.id, value)
//        thread {
//            val file = File(imageItem.url)
//            saveImageToExternal(file.name, value)
//        }
    }

    private fun exportPicturesToExternalStorage(filename: String, bitmap: Bitmap) {
        try {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Wallpaper")
            }
            val insertUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            val file = File(
                Environment.getExternalStorageDirectory(),
                "Pictures/Wallpaper/$filename"
            )
            if (!file.exists()) {
                insertUri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun get(image: ImageItem): Bitmap? {
        var bitmap = bitmapCache.get(image.id)
        if (bitmap == null) {
            bitmap = readBitmap(image.id)
            if (bitmap == null) {
                return null
            }
            bitmapCache.put(image.id, bitmap)
        }
        return bitmap
    }

    @WorkerThread
    private fun saveBitmap(filename: String, bitmap: Bitmap) {
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            Log.d(TAG, "${file.path} is exists")
            return
        }
        val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    @WorkerThread
    private fun readBitmap(filename: String): Bitmap? {
        val inputStream: InputStream?
        return try {
            inputStream = context.openFileInput(filename)
            val bitmap = inputStream?.use {
                BitmapFactory.decodeStream(it)
            }
            Log.d(TAG, "got a bitmap from file")
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val TAG = "WallpaperCache"
    }
}