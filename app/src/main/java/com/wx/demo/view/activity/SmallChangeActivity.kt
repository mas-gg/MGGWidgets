package com.wx.demo.view.activity

import com.wx.demo.BaseActivity
import android.os.Bundle
import com.wx.demo.common.CashManager
import com.wx.demo.databinding.ActivitySmallChangeBinding

/**
 * 零钱
 */
class SmallChangeActivity : BaseActivity() {
    private lateinit var binding: ActivitySmallChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmallChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvCash.text = "${CashManager.initCashValue}"
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onRestart() {
        super.onRestart()
        binding.tvCash.postDelayed({
            binding.tvCash.text = "${CashManager.totalValue}"
        },400)
    }
}