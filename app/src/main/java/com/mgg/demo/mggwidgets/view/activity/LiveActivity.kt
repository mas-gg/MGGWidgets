package com.mgg.demo.mggwidgets.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.mgg.demo.mggwidgets.BaseActivity
import com.mgg.demo.mggwidgets.R
import com.mgg.demo.mggwidgets.databinding.ActivityLiveBinding
import com.mgg.demo.mggwidgets.view.fragment.LiveFragment

class LiveActivity : BaseActivity<ActivityLiveBinding>() {

    companion object {
        @JvmStatic
        fun start(activity: AppCompatActivity){
            var intent=Intent(activity, LiveActivity::class.java)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        initFragment()
    }

    private fun initFragment() {
        var fragment = LiveFragment()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commitAllowingStateLoss()
    }
}
