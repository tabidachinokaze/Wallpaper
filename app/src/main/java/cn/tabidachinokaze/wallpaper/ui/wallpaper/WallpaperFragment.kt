package cn.tabidachinokaze.wallpaper.ui.wallpaper

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.tabidachinokaze.wallpaper.R
import cn.tabidachinokaze.wallpaper.base.adapter.WallpaperAdapter
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.databinding.FragmentWallpaperBinding
import cn.tabidachinokaze.wallpaper.repository.WallpaperRepository
import java.io.File
import java.util.*

class WallpaperFragment : Fragment() {
    private val wallpaperViewModel by lazy {
        ViewModelProvider(this).get(WallpaperViewModel::class.java)
    }
    private lateinit var repository: WallpaperRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: WallpaperAdapter
    private lateinit var images: MutableList<ImageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = wallpaperViewModel.wallpaperRepository
        images = wallpaperViewModel.images
    }

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
        swipeRefreshLayout = binding.swipeRefreshLayout
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        adapter = WallpaperAdapter(requireContext(), images)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            getResponse()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getResponse() {
        repository.getImageSource {
            val file = File(it)
            val image = ImageItem(
                UUID.randomUUID().toString(),
                file.name.removeSuffix(".jpg"),
                "",
                it,
                ""
            )
            images.add(0, image)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            Log.d(TAG, "lambda: $it")
        }
    }

    companion object {
        private const val TAG = "WallpaperFragment"
        fun getInstance() = WallpaperFragment()
    }
}