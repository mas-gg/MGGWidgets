package com.mgg.demo.mggwidgets

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgg.demo.mggwidgets.util.CalendarItem
import com.mgg.demo.mggwidgets.util.CalendarUtils
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var mAdapter: CalendarAdapter
    private lateinit var currentDate: Date

    companion object {
        @JvmStatic
        fun start(mContext: Context) {
            val intent = Intent(mContext, CalendarActivity::class.java)
            mContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        initView()
        initData()
    }

    private fun initView(){

    }

    private fun initData(){
        mAdapter = CalendarAdapter()
        recycler.layoutManager = GridLayoutManager(this, 7)
        recycler.adapter = mAdapter
        currentDate = Date()
        showCalendar(currentDate)
    }

    fun onLastMonthClick(view: View){
        var cal = Calendar.getInstance()
        cal.time = currentDate
        cal.set(Calendar.DAY_OF_MONTH,1)
        cal.add(Calendar.MONTH,-1)
        currentDate=cal.time
        showCalendar(currentDate)
    }

    fun onNextMonthClick(view: View){
        var cal = Calendar.getInstance()
        cal.time = currentDate
        cal.set(Calendar.DAY_OF_MONTH,1)
        cal.add(Calendar.MONTH,1)
        currentDate=cal.time
        showCalendar(currentDate)
    }

    /**
     * 显示当月日历
     */
    private fun showCalendar(date: Date) {
        tv_month.text = CalendarUtils.getMonthString(date)
        val list = CalendarUtils.getMonthCalendar(date)
        mAdapter.setData(list)
    }


    inner class CalendarAdapter : RecyclerView.Adapter<CalendarViewHolder>() {
        private lateinit var list: List<CalendarItem>
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
            var view = layoutInflater.inflate(R.layout.item_calendar, parent, false)
            return CalendarViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
            if (list[position].otherMonth) {
                holder.tvDay.text = ""
            } else {
                holder.tvDay.text = list[position].calendar.get(Calendar.DAY_OF_MONTH).toString()
                if (list[position].calendar.get(Calendar.DAY_OF_WEEK) == 7
                        || list[position].calendar.get(Calendar.DAY_OF_WEEK) == 1) {
                    holder.tvDay.setTextColor(Color.parseColor("#FF4081"))
                } else {
                    holder.tvDay.setTextColor(Color.parseColor("#f5A623"))
                }
            }

        }

        fun setData(list: List<CalendarItem>) {
            if (list?.size == 0) return
            this.list = list
            notifyDataSetChanged()
        }

    }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDay: TextView = view.findViewById(R.id.tv_day) as TextView
    }
}