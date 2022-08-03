package com.wx.demo

import android.app.Application
import android.content.Context
import com.wx.demo.common.CashManager

class BaseApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        CashManager.resetWxCash()
    }
}