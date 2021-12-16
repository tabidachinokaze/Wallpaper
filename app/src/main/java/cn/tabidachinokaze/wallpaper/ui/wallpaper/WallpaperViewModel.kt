package cn.tabidachinokaze.wallpaper.ui.wallpaper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.repository.WallpaperRepository

class WallpaperViewModel : ViewModel() {
    private val repository = WallpaperRepository.getInstance()
    val images: MutableList<ImageItem> = mutableListOf()
    val imagesLiveData: LiveData<List<ImageItem>> = repository.getImages()
}