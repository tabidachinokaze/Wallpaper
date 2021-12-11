package cn.tabidachinokaze.wallpaper.ui.wallpaper

import androidx.lifecycle.ViewModel
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.repository.WallpaperRepository

class WallpaperViewModel : ViewModel() {
    val wallpaperRepository = WallpaperRepository()
    val images: MutableList<ImageItem> = mutableListOf()
}