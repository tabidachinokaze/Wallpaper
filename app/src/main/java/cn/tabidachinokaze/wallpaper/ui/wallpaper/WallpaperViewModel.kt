package cn.tabidachinokaze.wallpaper.ui.wallpaper

import androidx.lifecycle.ViewModel
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem

class WallpaperViewModel : ViewModel() {
    val images: MutableList<ImageItem> = mutableListOf()
}