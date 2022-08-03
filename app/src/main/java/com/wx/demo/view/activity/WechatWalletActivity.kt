package com.wx.demo.view.activity

import android.annotation.SuppressLint
import com.wx.demo.BaseActivity
import android.os.Bundle
import com.wx.demo.R
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.wx.demo.common.CashManager

/**
 * 钱包
 */
class WechatWalletActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wechat_wallet)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val llCash = findViewById<LinearLayout>(R.id.ll_cash)
        val tvCash = findViewById<TextView>(R.id.tv_cash)
        val ivBack = findViewById<AppCompatImageView>(R.id.iv_back)
        ivBack.setOnClickListener{
            finish()
        }
        tvCash.text = "¥" + CashManager.getWxCash()
        llCash.setOnClickListener {
            startActivity(Intent(this@WechatWalletActivity, SmallChangeActivity::class.java))
        }
    }

}