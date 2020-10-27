package com.mgg.demo.mggwidgets

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.mgg.demo.mggwidgets.fragment.LiveFragment

class LiveActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(activity: AppCompatActivity){
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
