package com.afzal.dogwallpaper

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View

/**
 * Превью сцены прямо в Activity: та же анимация, что и на обоях.
 */
class DogPreviewView(context: Context) : View(context) {

    private val scene = DogScene()
    private var lastNs = 0L

    private val ticker = object : Runnable {
        override fun run() {
            val now = System.nanoTime()
            val dt = if (lastNs == 0L) 0.016f else ((now - lastNs) / 1_000_000_000.0).toFloat()
            lastNs = now
            scene.update(dt)
            invalidate()
            postOnAnimation(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lastNs = 0L
        postOnAnimation(ticker)
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(ticker)
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scene.onSizeChanged(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        scene.draw(canvas, width.toFloat(), height.toFloat())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (width > 0) scene.tap((event.x / width).coerceIn(0f, 1f))
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }
}
