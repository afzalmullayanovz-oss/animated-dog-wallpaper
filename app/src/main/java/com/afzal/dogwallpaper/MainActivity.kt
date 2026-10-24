package com.afzal.dogwallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * Экран приложения: живое превью собаки и кнопка установки обоев.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FrameLayout(this)
        val preview = DogPreviewView(this)
        root.addView(
            preview,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(20), dp(18), dp(20), dp(22))
            setBackgroundColor(0x99000000.toInt())
        }

        val title = TextView(this).apply {
            text = getString(R.string.app_name)
            setTextColor(Color.WHITE)
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        val hint = TextView(this).apply {
            text = getString(R.string.preview_hint)
            setTextColor(0xDDFFFFFF.toInt())
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, dp(8), 0, dp(14))
        }

        val button = Button(this).apply {
            text = getString(R.string.install_wallpaper)
            setOnClickListener { openWallpaperPicker() }
        }

        panel.addView(title)
        panel.addView(hint)
        panel.addView(button)

        val panelParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.BOTTOM }
        root.addView(panel, panelParams)

        setContentView(root)
    }

    private fun openWallpaperPicker() {
        val component = ComponentName(this, DogWallpaperService::class.java)
        val direct = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(direct)
            return
        } catch (_: ActivityNotFoundException) {
            // на некоторых прошивках нет прямого действия — открываем общий выбор обоев
        }
        try {
            startActivity(Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.no_wallpaper_picker), Toast.LENGTH_LONG).show()
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
