package com.mgg.demo.mggwidgets.util

import java.io.Serializable
import java.util.*

/**
 * Created by mgg
 */
class CalendarItem(var calendar: Calendar//公历日期
                   , var otherMonth: Boolean//其他月份
) : Serializable {
    override fun equals(obj: Any?): Boolean {
        return if (obj is CalendarItem) {
            val date = obj as CalendarItem?
            calendar == date!!.calendar
        } else false
    }
}
