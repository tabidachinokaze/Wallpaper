package cn.tabidachinokaze.wallpaper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.tabidachinokaze.wallpaper.data.dao.ImageDao
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem

@Database(entities = [ImageItem::class], version = 1, exportSchema = true)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}