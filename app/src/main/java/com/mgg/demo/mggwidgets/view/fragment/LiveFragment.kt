package com.mgg.demo.mggwidgets.view.fragment

import android.animation.*
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.mgg.demo.mggwidgets.R
import com.mgg.demo.mggwidgets.util.DensityUtils
import com.mgg.demo.mggwidgets.view.widgets.ThrowEggsView
import com.mgg.demo.mggwidgets.view.widgets.LiveItemView
import kotlinx.android.synthetic.main.fragment_live.*
import kotlinx.android.synthetic.main.layout_live_item.view.*


class LiveFragment : Fragment() {
    private var currentView: LiveItemView? = null
    private var margin2dp = 2
    private var canClick = true
    private lateinit var interpolator: Interpolator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_close.setOnClickListener {
            activity?.finish()
        }
        live_teacher?.setName("teacher")
        live_me?.setName("me")
        live1?.setName("1")
        live2?.setName("2")
        live3?.setName("3")
        interpolator= LinearInterpolator()
        radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.linearInterpolator -> interpolator= LinearInterpolator()
                R.id.accelerateInterpolator -> interpolator= AccelerateInterpolator()
                R.id.decelerateInterpolator -> interpolator= DecelerateInterpolator()
                R.id.accelerateDecelerateInterpolator -> interpolator= AccelerateDecelerateInterpolator()
                R.id.anticipateInterpolator -> interpolator= AnticipateInterpolator()
                R.id.overshootInterpolator -> interpolator= OvershootInterpolator()
                R.id.anticipateOvershootInterpolator -> interpolator= AnticipateOvershootInterpolator()
            }
        }

        margin2dp = DensityUtils.dp2px(activity, 2f)

        iv_egg.setOnClickListener {
            throwEgg(it)
        }

        live_me.tv_name.setOnClickListener {
            throwEgg(it)
        }
        live_me?.setOnClickListener {
            if (!canClick || currentView != null) return@setOnClickListener
            trans2View(live_me, live_teacher, object : AnimatorStateListener {
                override fun onAnimationStart() {
                    Log.d("", "${live_me.tv_name.layoutParams}")
                }

                override fun onAnimationEnd() {
                    setTeacherLayoutParams(live_me)
                    setMeLayoutParams(live_teacher)
                    Log.d("", "${live_me.tv_name.layoutParams}")
                }
            })

        }
        live1.tv_name.setOnClickListener {
            throwEgg(it)
        }
        live1?.setOnClickListener {
            if (!canClick || currentView != null) return@setOnClickListener
            trans2View(live1, live_teacher, object : AnimatorStateListener {
                override fun onAnimationStart() {
                }

                override fun onAnimationEnd() {
                    setTeacherLayoutParams(live1)
                    setS1LayoutParams(live_teacher)
                }
            })
        }

        live2.tv_name.setOnClickListener {
            throwEgg(it)
        }
        live2?.setOnClickListener {
            if (!canClick || currentView != null) return@setOnClickListener
            trans2View(live2, live_teacher, object : AnimatorStateListener {
                override fun onAnimationStart() {
                }

                override fun onAnimationEnd() {
                    setTeacherLayoutParams(live2)
                    setS2LayoutParams(live_teacher)
                }
            })
        }

        live3.tv_name.setOnClickListener {
            throwEgg(it)
        }
        live3?.setOnClickListener {
            if (!canClick || currentView != null) return@setOnClickListener
            trans2View(live3, live_teacher, object : AnimatorStateListener {
                override fun onAnimationStart() {
                }

                override fun onAnimationEnd() {
                    setTeacherLayoutParams(live3)
                    setS3LayoutParams(live_teacher)
                }
            })

        }

        live_teacher.setOnClickListener {
            if (!canClick || currentView == null) return@setOnClickListener
            trans2View(live_teacher, currentView!!, object : AnimatorStateListener {
                override fun onAnimationStart() {
                    Log.d("", "${live_me.tv_name.layoutParams}")
                }

                override fun onAnimationEnd() {
                    when (currentView) {
                        live_me -> {
                            setTeacherLayoutParams(live_teacher)
                            setMeLayoutParams(live_me)
                        }
                        live1 -> {
                            setTeacherLayoutParams(live_teacher)
                            setS1LayoutParams(live1)
                        }
                        live2 -> {
                            setTeacherLayoutParams(live_teacher)
                            setS2LayoutParams(live2)
                        }
                        live3 -> {
                            setTeacherLayoutParams(live_teacher)
                            setS3LayoutParams(live3)
                        }
                        else -> {
                        }
                    }
                    currentView = null
                    Log.d("", "${live_me.tv_name.layoutParams}")
                }

            })
        }

    }


    private fun getScreenPoint(view: View): PointF {
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        return PointF(location[0].toFloat(), location[1].toFloat())
    }

    private fun throwEgg(view:View){
        val startPoint=getScreenPoint(view)
        val endPoint=getScreenPoint(live_teacher)

        var imageView = ThrowEggsView(activity)
        imageView.setImageResource(R.drawable.ic_egg)
        val layoutParams= LinearLayout.LayoutParams(iv_egg.width,iv_egg.height)
        imageView.layoutParams=layoutParams

        var viewGroup=activity?.window?.decorView as ViewGroup
        viewGroup.addView(imageView)

        startPoint.x +=view.width/2-iv_egg.width/2
        startPoint.y +=view.height/2-iv_egg.height/2

        endPoint.x +=live_teacher.width/2-iv_egg.width/2
        endPoint.y +=live_teacher.height/2-iv_egg.height/2
        imageView.start(startPoint,endPoint,0)
    }

    private fun trans2View(view1: View, view2: View, listener: AnimatorStateListener?) {

        val location1 = IntArray(2)
        view1.getLocationOnScreen(location1)
        val x1 = location1[0] + view1.width / 2 //中心点 x坐标
        val y1 = location1[1] + view1.height / 2 //中心点 y坐标

        val location2 = IntArray(2)
        view2.getLocationOnScreen(location2)
        val x2 = location2[0] + view2.width / 2
        val y2 = location2[1] + view2.height / 2

        val width1 = view1.width.toFloat()
        val height1 = view1.height.toFloat()
        val width2 = view2.width.toFloat()
        val height2 = view2.height.toFloat()

        view1.animate()
                .setDuration(500)
                .setInterpolator(interpolator)
                .translationX((x2 - x1).toFloat())
                .translationY((y2 - y1).toFloat())
                .scaleX(width2 / width1)
                .scaleY(height2 / height1)
                .start()

        view2.animate()
                .setDuration(500)
                .setInterpolator(interpolator)
                .translationX((x1 - x2).toFloat())
                .translationY((y1 - y2).toFloat())
                .scaleX(width1 / width2)
                .scaleY(height1 / height2)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view1.scaleX = 1f
                        view1.scaleY = 1f
                        view1.translationX = 0f
                        view1.translationY = 0f
                        view2.scaleX = 1f
                        view2.scaleY = 1f
                        view2.translationX = 0f
                        view2.translationY = 0f
                        listener?.onAnimationEnd()
                        view2.animate().setListener(null)
                        canClick = true
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        canClick = false
                        listener?.onAnimationStart()
                    }
                })
                .start()
    }

    private fun trans2ViewSmooth(view1: View, view2: View, listener: AnimatorStateListener?) {

        val location1 = IntArray(2)
        view1.getLocationOnScreen(location1)
        val x1 = location1[0] + view1.width / 2 //中心点 x坐标
        val y1 = location1[1] + view1.height / 2 //中心点 y坐标

        val location2 = IntArray(2)
        view2.getLocationOnScreen(location2)
        val x2 = location2[0] + view2.width / 2
        val y2 = location2[1] + view2.height / 2

        val width1 = view1.width.toFloat()
        val height1 = view1.height.toFloat()
        val width2 = view2.width.toFloat()
        val height2 = view2.height.toFloat()

        view1.animate()
                .setDuration(500)
                .scaleX(1f)
                .setInterpolator(object : LinearInterpolator(){
                    override fun getInterpolation(input: Float): Float {
                        var layoutParams1 = view1.layoutParams as LinearLayout.LayoutParams
                        layoutParams1.width += (input *(width2-width1)).toInt()
                        layoutParams1.height += (input *(height2-height1)).toInt()
                        view1.layoutParams = layoutParams1

                        var layoutParams2 = view2.layoutParams as LinearLayout.LayoutParams
                        layoutParams2.width += (input *(width1-width2)).toInt()
                        layoutParams2.height += (input *(height1-height2)).toInt()
                        view2.layoutParams = layoutParams2
                        return super.getInterpolation(input)
                    }
                })
//                .setListener(object : Animator.AnimatorListener {
//                    override fun onAnimationRepeat(animation: Animator?) {
//                    }
//
//                    override fun onAnimationEnd(animation: Animator?) {
//                        view1.scaleX = 1f
//                        view1.scaleY = 1f
//                        view1.translationX = 0f
//                        view1.translationY = 0f
//                        view2.scaleX = 1f
//                        view2.scaleY = 1f
//                        view2.translationX = 0f
//                        view2.translationY = 0f
//                        listener?.onAnimationEnd()
//                        view2.animate().setListener(null)
//                        canClick = true
//                    }
//
//                    override fun onAnimationCancel(animation: Animator?) {
//                    }
//
//                    override fun onAnimationStart(animation: Animator?) {
//                        canClick = false
//                        listener?.onAnimationStart()
//                    }
//                })
                .start()
    }

    interface AnimatorStateListener {
        fun onAnimationStart()
        fun onAnimationEnd()
    }

    private fun setTeacherLayoutParams(view: View) {
        currentView = view as LiveItemView
        ll1.removeView(view)
        ll2.removeView(view)
        ll_all.addView(view, 0)
        var layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = MATCH_PARENT
        layoutParams.height = 0
        layoutParams.weight = 3f
        layoutParams.setMargins(margin2dp, margin2dp, margin2dp, margin2dp)
        view.layoutParams = layoutParams
    }

    private fun setMeLayoutParams(view: View) {
        ll_all.removeView(view)
        ll1.addView(view, 0)
        var layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = 0
        layoutParams.height = MATCH_PARENT
        layoutParams.weight = 1f
        layoutParams.setMargins(0, 0, margin2dp, 0)
        view.layoutParams = layoutParams
    }

    private fun setS1LayoutParams(view: View) {
        ll_all.removeView(view)
        ll1.addView(view)
        var layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = 0
        layoutParams.height = MATCH_PARENT
        layoutParams.weight = 1f
        layoutParams.setMargins(0, 0, margin2dp, 0)
        view.layoutParams = layoutParams
    }

    private fun setS2LayoutParams(view: View) {
        ll_all.removeView(view)
        ll2.addView(view, 0)
        var layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = 0
        layoutParams.height = MATCH_PARENT
        layoutParams.weight = 1f
        layoutParams.setMargins(0, 0, margin2dp, 0)
        view.layoutParams = layoutParams
    }

    private fun setS3LayoutParams(view: View) {
        ll_all.removeView(view)
        ll2.addView(view)
        var layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = 0
        layoutParams.height = MATCH_PARENT
        layoutParams.weight = 1f
        layoutParams.setMargins(0, 0, margin2dp, 0)
        view.layoutParams = layoutParams
    }

}