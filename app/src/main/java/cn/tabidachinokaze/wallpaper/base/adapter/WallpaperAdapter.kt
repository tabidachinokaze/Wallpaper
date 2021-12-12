package cn.tabidachinokaze.wallpaper.base.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.wallpaper.R
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.databinding.ImageItemBinding
import cn.tabidachinokaze.wallpaper.handler.PictureLoader

class WallpaperAdapter(
    private val context: Context,
    private val pictureLoader: PictureLoader<ImageHolder>,
    private val images: List<ImageItem>
) :
    RecyclerView.Adapter<ImageHolder>() {
    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding =
            DataBindingUtil.inflate<ImageItemBinding>(inflater, R.layout.image_item, parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val image = images[position]
        holder.bindTitle(image.title)
        val placeholder: Drawable =
            ContextCompat.getDrawable(context, R.drawable.placeholder)
                ?: ColorDrawable()
        holder.bindDrawable(placeholder)
        pictureLoader.enterQueue(holder, image)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}