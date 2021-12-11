package cn.tabidachinokaze.wallpaper.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageItem(
    @PrimaryKey var id: String,
    var title: String,
    var content: String,
    var url: String,
    var path: String
)