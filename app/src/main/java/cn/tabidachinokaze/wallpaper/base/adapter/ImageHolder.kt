package cn.tabidachinokaze.wallpaper.base.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.wallpaper.databinding.ImageItemBinding

class ImageHolder(private val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val imageTitle: TextView = binding.imageTitle
    private val imageView: ImageView = binding.image
    val bindTitle: (String) -> Unit = imageTitle::setText
    val bindDrawable: (Drawable) -> Unit = imageView::setImageDrawable
}