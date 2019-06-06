package com.mgg.demo.mggwidgets

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_svg.*

class SvgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svg)
    }

    fun onAndroidClick(view: View) {
        var animate = iv_anim_android.drawable as Animatable
        if (animate.isRunning) {
            animate.stop()
        } else {
            animate.start()
        }
    }

    fun onRectangleClick(view: View) {
        var animate = iv_anim_rectangle.drawable as Animatable
        if (animate.isRunning) {
            animate.stop()
        } else {
            animate.start()
        }
    }

    fun onTriangleClick(view: View) {
        var animate = iv_anim_triangle.drawable as Animatable
        if (animate.isRunning) {
            animate.stop()
        } else {
            animate.start()
        }
    }
}