package cn.tabidachinokaze.wallpaper.ui.wallpaper

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.tabidachinokaze.wallpaper.R
import cn.tabidachinokaze.wallpaper.base.adapter.ImageHolder
import cn.tabidachinokaze.wallpaper.base.adapter.WallpaperAdapter
import cn.tabidachinokaze.wallpaper.data.entities.ImageItem
import cn.tabidachinokaze.wallpaper.databinding.FragmentWallpaperBinding
import cn.tabidachinokaze.wallpaper.handler.PictureLoader
import cn.tabidachinokaze.wallpaper.repository.WallpaperRepository
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

class WallpaperFragment : Fragment() {
    private val wallpaperViewModel by lazy {
        ViewModelProvider(this).get(WallpaperViewModel::class.java)
    }
    private val repository = WallpaperRepository.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: WallpaperAdapter
    private lateinit var images: MutableList<ImageItem>
    private lateinit var pictureLoader: PictureLoader<ImageHolder>
    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        images = wallpaperViewModel.images
        val responseHolder = Handler(Looper.getMainLooper())
        pictureLoader = PictureLoader(responseHolder) { imageHolder, imageItem, bitmap ->
            imageHolder.bindDrawable(BitmapDrawable(resources, bitmap))
            repository.cache.put(imageItem, bitmap)
        }
        lifecycle.addObserver(pictureLoader.fragmentLifecycleObserver)
        Log.d(TAG, "cacheDir ${context?.cacheDir}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) {
            it.lifecycle.addObserver(pictureLoader.viewLifecycleObserver)
        }
        val binding = DataBindingUtil.inflate<FragmentWallpaperBinding>(
            inflater,
            R.layout.fragment_wallpaper,
            container,
            false
        )
        snackBar = Snackbar.make(binding.root, "加载中···", Snackbar.LENGTH_INDEFINITE)
            .setAction("取消") {
                Toast.makeText(context, "取消加载", Toast.LENGTH_SHORT).show()
            }
        binding.floatingActionButton.setOnClickListener {
            repeat(4) {
                getResponse()
            }
        }
        swipeRefreshLayout = binding.swipeRefreshLayout
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        adapter = WallpaperAdapter(requireContext(), pictureLoader, images)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            repeat(6) {
                getResponse()
            }
        }
        wallpaperViewModel.imagesLiveData.observe(viewLifecycleOwner) {
            images.addAll(0, it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(pictureLoader.viewLifecycleObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(pictureLoader.fragmentLifecycleObserver)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getResponse() {
        snackBar.show()
        repository.getImageSource {
            val file = File(it)
            val image = ImageItem(
                UUID.randomUUID().toString(),
                file.name.removeSuffix(".jpg"),
                "",
                it,
                ""
            )
            if (repository.getImageByUrl(it) == null) {
                repository.addImage(image)
            }
            images.add(0, image)
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            if (snackBar.isShown) {
                snackBar.dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "WallpaperFragment"
        fun getInstance() = WallpaperFragment()
    }
}