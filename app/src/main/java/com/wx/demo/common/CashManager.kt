package com.wx.demo.common

import android.content.ContentValues
import android.net.Uri
import com.wx.demo.BaseApplication

object CashManager {

    private const val WX_CASH_KEY = "wx_cash"
    private const val initCashValue = 0.3

    fun resetWxCash() {
        updateWxCash()
    }

    private fun updateWxCash(value: Double = initCashValue) {
        val content = ContentValues().apply {
            put("_key", WX_CASH_KEY)
            put("value", value)
        }
        val rows = BaseApplication.instance.contentResolver.update(Uri.parse(CashProvider.CASH), content, null, null)
        if (rows == 0) {
            BaseApplication.instance.contentResolver.insert(Uri.parse(CashProvider.CASH), content)
        }
    }

    fun getWxCash(): Double {
        var cash = initCashValue
        BaseApplication.instance.contentResolver.query(Uri.parse("${CashProvider.CASH}/$WX_CASH_KEY"), null, null, null, null)?.run {
            while (this.moveToNext()) {
                cash = this.getDouble(this.getColumnIndex("value"))
            }
            this.close()
        }
        return cash
    }
}