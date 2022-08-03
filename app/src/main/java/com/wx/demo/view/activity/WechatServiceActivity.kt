package com.wx.demo.view.activity

import com.wx.demo.BaseActivity
import android.os.Bundle
import com.wx.demo.R
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.wx.demo.common.CashManager

/**
 * 服务
 */
class WechatServiceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wechat_service)
        initView()
    }

    private fun initView() {
        val tvWallet = findViewById<TextView>(R.id.tv_wallet)
        val tvCash = findViewById<TextView>(R.id.tv_cash)
        tvCash.text = "¥"+ CashManager.getWxCash()
        tvWallet.setOnClickListener {
            startActivity(Intent(this@WechatServiceActivity, WechatWalletActivity::class.java))
        }
    }
}