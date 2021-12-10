package cn.tabidachinokaze.wallpaper.ui.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.wallpaper.R
import cn.tabidachinokaze.wallpaper.base.adapter.WallpaperAdapter
import cn.tabidachinokaze.wallpaper.databinding.FragmentWallpaperBinding

class WallpaperFragment : Fragment() {
    private val wallpaperViewModel by lazy {
        ViewModelProvider(this).get(WallpaperViewModel::class.java)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WallpaperAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWallpaperBinding>(
            inflater,
            R.layout.fragment_wallpaper,
            container,
            false
        )
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    companion object {
        fun getInstance() = WallpaperFragment()
    }
}