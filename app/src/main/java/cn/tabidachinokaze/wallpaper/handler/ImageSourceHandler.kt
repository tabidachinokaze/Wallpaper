package cn.tabidachinokaze.wallpaper.handler

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import java.util.concurrent.ConcurrentSkipListSet

class ImageSourceHandler : HandlerThread(TAG) {
    private lateinit var requestHandler: Handler
    private val requestList = ConcurrentSkipListSet<String>()

    override fun onLooperPrepared() {
        requestHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MESSAGE_LOAD -> {
                        Log.d(TAG, msg.obj as String)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun quit(): Boolean {
        true
        return super.quit()
    }

    fun enterQueue(obj: String, count: Int) {
        requestHandler.obtainMessage(MESSAGE_LOAD, obj).sendToTarget()
    }

    fun handleRequest(obj: String) {

    }

    companion object {
        private const val TAG = "ImageSourceHandler"
        private const val MESSAGE_LOAD = 0
    }
}