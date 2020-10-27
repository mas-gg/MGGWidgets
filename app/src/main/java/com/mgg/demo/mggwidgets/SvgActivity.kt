package com.mgg.demo.mggwidgets

import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_svg.*

class SvgActivity : BaseActivity() {
    var pinAnimEnd=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svg)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var animate = iv_anim_pin.drawable as AnimatedVectorDrawable
            animate.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    if (!pinAnimEnd){
                        animate.start()
                    }
                }
            })
        }
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

    fun onPinClick(view: View) {
        var animate = iv_anim_pin.drawable as Animatable
        if (animate.isRunning) {
            pinAnimEnd=true
            animate.stop()
        } else {
            pinAnimEnd=false
            animate.start()
        }
    }
}