package cn.tabidachinokaze.wallpaper.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem

@Dao
interface ImageDao {
    @Insert
    fun addImage(imageItem: ImageItem)

    @Delete
    fun deleteImage(imageItem: ImageItem)

    @Update
    fun updateImage(imageItem: ImageItem)

    @Query("select * from ImageItem where id=(:id)")
    fun getImage(id: String): ImageItem?

    @Query("select * from ImageItem where url=(:url)")
    fun getImageByUrl(url: String): ImageItem?

    @Query("select * from ImageItem")
    fun getImages(): LiveData<List<ImageItem>>
}