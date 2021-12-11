package cn.tabidachinokaze.wallpaper.handler

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.ConcurrentHashMap

class ImageSourceHandler<in T> : HandlerThread(TAG) {
    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<String, Int>()
    companion object {
        private const val TAG = "ImageSourceHandler"
    }
}