package com.mgg.demo.mggwidgets.util;


import java.util.*

/**
 * Created by mgg on 2018/6/15.
 */

object CalendarUtils {
    //周的第一天
    const val SUNDAY = 0//周的第一天 周日
    const val MONDAY = 1//周的第一天 周一
    private val month_english = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    fun getMonthString(date: Date): String {
        var cal = Calendar.getInstance()
        cal.time = date
        return month_english[cal.get(Calendar.MONTH)]
    }

    /**
     * @param date 今天
     * @param type 300，周日，301周一
     * @return
     */
    @JvmOverloads
    fun getMonthCalendar(date: Date, type: Int = MONDAY): List<CalendarItem> {
        var cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val days = cal.getActualMaximum(Calendar.DATE)//当月天数

        cal.add(Calendar.MONTH, -1)
        val lastMonthDays = cal.getActualMaximum(Calendar.DATE)//上个月的天数

        cal.add(Calendar.MONTH, 1)
        var firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1//当月第一天周几
        if (firstDayOfWeek==0)firstDayOfWeek=7

        cal.add(Calendar.MONTH, 1)
        var temp = cal.get(Calendar.DAY_OF_WEEK)-1
        if (temp==0)temp=7
        var endDayOfWeek = temp - 1//当月最后一天周几
        if (endDayOfWeek==0){
            endDayOfWeek=7
        }


        val dateList = ArrayList<CalendarItem>()

        //周一开始的
        if (type == MONDAY) {
            for (i in 0 until firstDayOfWeek - 1) {
                val temp = Calendar.getInstance()
                temp.time = date
                temp.set(Calendar.DAY_OF_MONTH, 1)
                temp.add(Calendar.MONTH, -1)
                temp.set(Calendar.DAY_OF_MONTH, lastMonthDays - (firstDayOfWeek - i - 2))
                val d=temp.time
                dateList.add(CalendarItem(temp, true))
            }
            for (i in 0 until days) {
                val temp = Calendar.getInstance()
                temp.time = date
                temp.set(Calendar.DAY_OF_MONTH, i + 1)
                val d=temp.time
                dateList.add(CalendarItem(temp, false))
            }
            for (i in 0 until 7 - endDayOfWeek) {
                val temp = Calendar.getInstance()
                temp.time = date
                temp.set(Calendar.DAY_OF_MONTH, 1)
                temp.add(Calendar.MONTH, 1)
                temp.set(Calendar.DAY_OF_MONTH, i + 1)
                val d=temp.time
                dateList.add(CalendarItem(temp, true))
            }

        } else {
            //上个月
            if (firstDayOfWeek != 7) {
                for (i in 0 until firstDayOfWeek) {
                    val temp = Calendar.getInstance()
                    temp.time = date
                    temp.set(Calendar.DAY_OF_MONTH, 1)
                    temp.add(Calendar.MONTH, -1)
                    temp.set(Calendar.DAY_OF_MONTH, lastMonthDays - (firstDayOfWeek - i - 1))
                    dateList.add(CalendarItem(temp, true))
                }
            }
            //当月
            for (i in 0 until days) {
                val temp = Calendar.getInstance()
                temp.time = date
                temp.set(Calendar.DAY_OF_MONTH, i + 1)
                dateList.add(CalendarItem(temp, false))
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0
            }
            for (i in 0 until 6 - endDayOfWeek) {
                val temp = Calendar.getInstance()
                temp.time = date
                temp.set(Calendar.DAY_OF_MONTH, 1)
                temp.add(Calendar.MONTH, 1)
                temp.set(Calendar.DAY_OF_MONTH, i + 1)
                dateList.add(CalendarItem(temp, true))
            }
        }
        return dateList

    }
}
