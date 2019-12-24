package com.mgg.demo.mggwidgets

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * created by mgg
 */
open class BaseActivity : AppCompatActivity(){

    val TAG = javaClass.name!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}