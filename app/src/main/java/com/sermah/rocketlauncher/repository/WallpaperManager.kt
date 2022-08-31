package com.sermah.rocketlauncher.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap


object WallpaperManager {
    private var _wallpaper: Drawable? = null
    private var _isWallpaperDark: Boolean = false
    val wallpaper: Drawable? get() = _wallpaper
    val isWallpaperLoaded get() = _wallpaper != null
    val isWallpaperDark get() = _isWallpaperDark

    @SuppressLint("MissingPermission")
    fun loadWallpaper(context: Context) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        _wallpaper = wallpaperManager.drawable
        if (wallpaper != null) _isWallpaperDark = isWallpaperDark(_wallpaper!!)
    }

    private fun isWallpaperDark(wallpaper: Drawable): Boolean {
        val bitmap = wallpaper.toBitmap(128, 128, Bitmap.Config.ARGB_8888)
        val darkThreshold = bitmap.width * bitmap.height * 0.45f
        val w = bitmap.width
        val h = bitmap.height

        var darkPixels = 0
        val lineColor = IntArray(w)
        for (i in 0 until h-1) {
            bitmap.getPixels(lineColor, 0, w, 0, i, w, 1)
            for (color in lineColor) {
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)
                val luminance = (0.299f * r + 0.0f + 0.587f * g + 0.0f + 0.114f * b + 0.0f)
                if (luminance < 150) {
                    darkPixels++
                }
            }
        }

        return darkPixels >= darkThreshold
    }

}