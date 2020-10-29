package com.mgg.demo.mggwidgets.view.activity

import android.os.Bundle
import android.view.View
import com.mgg.demo.mggwidgets.BaseActivity
import org.junit.Test

/**
 * created by mgg
 */

class CoroutineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))
        initView()
        initData()
    }

    private fun initView() {

    }

    @Test
    fun initData() {

    }
}
