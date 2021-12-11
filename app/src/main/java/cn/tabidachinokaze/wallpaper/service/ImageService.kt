package cn.tabidachinokaze.wallpaper.service

import cn.tabidachinokaze.wallpaper.data.entities.ImageResponse
import retrofit2.Call
import retrofit2.http.GET

interface ImageService {

    @GET("random_img.php?return=json")
    fun getImageResponse(): Call<ImageResponse>
}