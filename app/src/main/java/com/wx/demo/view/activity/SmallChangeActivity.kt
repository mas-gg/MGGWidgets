package com.wx.demo.view.activity

import com.wx.demo.BaseActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.wx.demo.R
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
        binding.tvCash.text = "${CashManager.getWxCash()}"
        val ivBack = findViewById<AppCompatImageView>(R.id.iv_back)
        ivBack.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvCash.text = "${CashManager.getWxCash()}"
    }
}