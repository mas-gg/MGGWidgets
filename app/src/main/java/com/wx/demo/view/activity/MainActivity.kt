package com.wx.demo.view.activity

import com.wx.demo.BaseActivity
import android.os.Bundle
import com.wx.demo.R
import android.content.Intent
import android.view.View

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goSmallChangeActivity(view: View) {
        startActivity(Intent(this@MainActivity, SmallChangeActivity::class.java))
    }

}