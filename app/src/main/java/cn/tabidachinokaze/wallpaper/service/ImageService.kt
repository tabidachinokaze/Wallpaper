package cn.tabidachinokaze.wallpaper.service

import cn.tabidachinokaze.wallpaper.data.entities.ImageResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ImageService {

    @GET("random_img.php?return=json")
    fun getImageResponse(): Call<ImageResponse>

    @GET
    fun getUrlBytes(@Url url: String): Call<ResponseBody>
}