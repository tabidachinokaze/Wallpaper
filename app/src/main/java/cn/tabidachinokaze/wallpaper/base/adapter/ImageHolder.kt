package cn.tabidachinokaze.wallpaper.base.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.databinding.ImageItemBinding

class ImageHolder(private val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val imageTitle: TextView = binding.imageTitle
    fun bind(image: ImageItem) {
        imageTitle.text = image.title
    }
}