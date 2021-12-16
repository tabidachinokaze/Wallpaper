package cn.tabidachinokaze.wallpaper.handler

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.repository.WallpaperRepository
import java.util.concurrent.ConcurrentHashMap

class PictureLoader<in T>(
    private val responseHandler: Handler,
    private val onPictureLoaded: (T, ImageItem, Bitmap) -> Unit
) : HandlerThread(TAG) {

    val fragmentLifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, "Starting background thread")
                start()
                looper
            }
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "Destroying background thread")
                quit()
            }
            else -> {}
        }
    }
    val viewLifecycleObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "view Destroying")
                responseHandler.removeMessages(MESSAGE_DOWNLOAD)
                requestMap.clear()
            }
            else -> {}
        }
    }
    private var hasQuit = false
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, ImageItem>()
    private val repository = WallpaperRepository.getInstance()

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                Log.d(TAG, "msg " + msg.what.toString())
                when (msg.what) {
                    MESSAGE_DOWNLOAD -> {
                        val imageHolder = msg.obj as T
                        Log.d(TAG, "Start processing the request of ${requestMap[imageHolder]?.url}")
                        handleRequest(imageHolder)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun enterQueue(obj: T, imageItem: ImageItem) {
        Log.d(TAG, "${imageItem.title} enter queue")
        requestMap[obj] = imageItem
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, obj).sendToTarget()
    }

    private fun handleRequest(imageHolder: T) {
        val imageItem = requestMap[imageHolder] ?: return
        val bitmap = repository.getImageBitmap(imageItem.url) ?: return
        responseHandler.post(Runnable {
            if (requestMap[imageHolder] != imageItem || hasQuit) {
                return@Runnable
            }

            requestMap.remove(imageHolder)
            onPictureLoaded(imageHolder, imageItem, bitmap)
        })
    }

    companion object {
        private const val TAG = "PictureLoader"
        private const val MESSAGE_DOWNLOAD = 0
    }
}