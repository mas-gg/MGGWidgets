package com.mgg.demo.mggwidgets.view.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.mgg.demo.mggwidgets.R
import kotlinx.android.synthetic.main.layout_live_item.view.*

class LiveItemView : RelativeLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_live_item, this)
    }

    fun setName(string: String) {
        tv_name.text = string
    }

}
