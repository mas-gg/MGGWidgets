package com.mgg.demo.mggwidgets

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.mgg.demo.mggwidgets.fragment.LiveFragment

class LiveActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: Activity){
            var intent=Intent(activity,LiveActivity::class.java)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_live)
        initFragment()
    }

    private fun initFragment() {
        var fragment = LiveFragment()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commitAllowingStateLoss()
    }


}