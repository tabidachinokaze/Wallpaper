package cn.tabidachinokaze.wallpaper.data.entities

import cn.tabidachinokaze.wallpaper.model.ImageUrl

data class ImageResponse(
    val error: Int,
    val img: String,
    val result: Int
) : ImageUrl {
    override fun getImageUrl(): String {
        return "https:$img"
    }
}