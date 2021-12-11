package cn.tabidachinokaze.wallpaper.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.tabidachinokaze.wallpaper.data.entities.ImageResponse
import cn.tabidachinokaze.wallpaper.service.ImageService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WallpaperRepository {
    private val imageService: ImageService
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        imageService = retrofit.create(ImageService::class.java)
    }

    fun getImageSource(onResponded: (String) -> Unit) {
        Log.d(TAG, "getImageSource start")
        val responseLiveData: MutableLiveData<ImageResponse> = MutableLiveData()
        val request: Call<ImageResponse> = imageService.getImageResponse()
        request.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                Log.d(TAG, "Response received")
                val imageResponse: ImageResponse? = response.body()
                imageResponse?.let {
                    Log.d(TAG, it.img)
                    onResponded(it.getImageUrl())
                }
                responseLiveData.value = imageResponse
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.d(TAG, "Failed")
            }
        })
        Log.d(TAG, "getImageSource end")
    }

    companion object {
        private const val TAG = "WallpaperRepository"
        private const val baseUrl = "https://img.xjh.me/"
    }
}