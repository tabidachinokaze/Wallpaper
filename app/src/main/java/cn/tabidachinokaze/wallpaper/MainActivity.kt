package cn.tabidachinokaze.wallpaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.tabidachinokaze.wallpaper.ui.wallpaper.WallpaperFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = WallpaperFragment.getInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}