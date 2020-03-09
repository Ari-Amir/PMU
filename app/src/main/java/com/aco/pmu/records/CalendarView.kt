package com.aco.pmu.records


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aco.pmu.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarView : LinearLayout {
    private var dateTitle: TextView? = null
    private var leftButton: ImageView? = null
    private var rightButton: ImageView? = null
    private var baseRootView: View? = null
    private var calendarMonthLayout: ViewGroup? = null
    private var calendarListener: CalendarListener? = null
    private var currentCalendar = Calendar.getInstance()
    private var lastSelectedDayCalendar: Calendar? = null
    private val onDayOfMonthClickListener = OnClickListener { view ->
        // Extract day selected
        val dayOfTheMonthContainer = view as ViewGroup
        var tagId = dayOfTheMonthContainer.tag as String
        tagId = tagId.substring(DAY_OF_THE_MONTH_LAYOUT.length, tagId.length)
        val dayOfTheMonthText = view.findViewWithTag<TextView>(DAY_OF_THE_MONTH_TEXT + tagId)
        // Extract the day from the text
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = currentCalendar[Calendar.YEAR]
        calendar[Calendar.MONTH] = currentCalendar[Calendar.MONTH]
        calendar[Calendar.DAY_OF_MONTH] = Integer.valueOf(dayOfTheMonthText.text.toString())
        markDayAsSelectedDay(calendar.time)
        calendarListener!!.onDayClick(calendar.time)
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun init() {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        baseRootView = inflate.inflate(R.layout.calendar_view_layout, this, true)
        findViewsById(rootView)
        setUpEventListeners()
        date = currentCalendar.time
    }

    fun setCurrentDate(currentDate: Date) {
        date = currentDate
    }

        var date: Date
        get() = currentCalendar.time
        set(date) {
            currentCalendar.time = date
            updateView()
    }

    private fun findViewsById(view: View?) {
        calendarMonthLayout = view!!.findViewById(R.id.calendarDateTitleContainer)
        leftButton = view.findViewById(R.id.leftButton)
        rightButton = view.findViewById(R.id.rightButton)
        dateTitle = view.findViewById(R.id.monthText)
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (i in 0..41) {
            val weekIndex = i % 7 + 1
            val dayOfTheWeekLayout =
                view.findViewWithTag<ViewGroup>(DAY_OF_THE_WEEK_LAYOUT + weekIndex)
            // Create day of the month
            val dayOfTheMonthLayout =
                inflate.inflate(R.layout.calendar_day_of_the_month_layout, null)
            val dayOfTheMonthText = dayOfTheMonthLayout.findViewWithTag<View>(DAY_OF_THE_MONTH_TEXT)
            val dayOfTheMonthBackground =
                dayOfTheMonthLayout.findViewWithTag<View>(DAY_OF_THE_MONTH_BACKGROUND)
            val dayOfTheMonthCircleImage =
                dayOfTheMonthLayout.findViewWithTag<View>(DAY_OF_THE_MONTH_CIRCLE_IMAGE)
            // Set tags to identify them
            val viewIndex = i + 1
            dayOfTheMonthLayout.tag = DAY_OF_THE_MONTH_LAYOUT + viewIndex
            dayOfTheMonthText.tag = DAY_OF_THE_MONTH_TEXT + viewIndex
            dayOfTheMonthBackground.tag = DAY_OF_THE_MONTH_BACKGROUND + viewIndex
            dayOfTheMonthCircleImage.tag = DAY_OF_THE_MONTH_CIRCLE_IMAGE + viewIndex
            dayOfTheWeekLayout.addView(dayOfTheMonthLayout)
        }
    }

    fun setCalendarListener(calendarListener: CalendarListener?) {
        this.calendarListener = calendarListener
    }

    fun markDayAsSelectedDay(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        // Clear previous current day mark
        clearSelectedDay()
        // Store current values as last values
        lastSelectedDayCalendar = calendar
        // Mark current day as selected
        val dayOfTheMonthBackground = getDayOfMonthBackground(calendar)
        dayOfTheMonthBackground.setBackgroundResource(R.drawable.circle)
        val dayOfTheMonth = getDayOfMonthText(calendar)
        dayOfTheMonth.setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    fun clearSelectedDay() {
        if (lastSelectedDayCalendar != null) {
            val dayOfTheMonthBackground = getDayOfMonthBackground(lastSelectedDayCalendar!!)
            // If it's today, keep the current day style
            val nowCalendar = Calendar.getInstance()
            if (areInTheSameDay(nowCalendar, lastSelectedDayCalendar!!)) {
                dayOfTheMonthBackground.setBackgroundResource(R.drawable.ring)
            } else {
                dayOfTheMonthBackground.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    fun setUpEventListeners() {
        leftButton!!.setOnClickListener { view: View? ->
            currentCalendar.add(Calendar.MONTH, -1)
            lastSelectedDayCalendar = null
            updateView()
            calendarListener!!.onLeftButtonClick()
        }
        rightButton!!.setOnClickListener { view: View? ->
            currentCalendar.add(Calendar.MONTH, 1)
            lastSelectedDayCalendar = null
            updateView()
            calendarListener!!.onRightButtonClick()
        }
    }

    private fun setUpMonthLayout() {
        var sdf = SimpleDateFormat("LLLL YYYY", Locale.getDefault()).format(date)
        sdf = sdf.substring(0, 1).toUpperCase() + sdf.subSequence(1, sdf.length)
        dateTitle!!.text = String.format("%s %s", sdf, " г.")
    }

    private fun setUpWeekDaysLayout() {
        var dayOfWeek: TextView
        var dayOfTheWeekString: String
        val weekDaysArray = DateFormatSymbols(Locale("ru", "RU")).shortWeekdays
        val length = weekDaysArray.size
        for (i in 1 until length) {
            dayOfWeek =
                rootView!!.findViewWithTag(DAY_OF_THE_WEEK_TEXT + getWeekIndex(i, currentCalendar))
            dayOfTheWeekString = weekDaysArray[i]
            dayOfWeek.text = dayOfTheWeekString.substring(0, 1).toUpperCase() + dayOfTheWeekString.subSequence(1, dayOfTheWeekString.length)
        }
    }

    private fun setUpDaysOfMonthLayout() {
        var dayOfTheMonthText: TextView
        var circleImage: View
        var dayOfTheMonthContainer: ViewGroup
        var dayOfTheMonthBackground: ViewGroup
        for (i in 1..42) {
            dayOfTheMonthContainer = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i)
            dayOfTheMonthBackground = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_BACKGROUND + i)
            dayOfTheMonthText = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i)
            circleImage = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_CIRCLE_IMAGE + i)
            dayOfTheMonthText.visibility = View.INVISIBLE
            circleImage.visibility = View.GONE
            // Apply styles
            dayOfTheMonthText.setTypeface(null, Typeface.NORMAL)
            dayOfTheMonthText.setTextColor(ContextCompat.getColor(context, R.color.white))
            dayOfTheMonthContainer.setBackgroundResource(android.R.color.black)
            dayOfTheMonthContainer.setOnClickListener(null)
            dayOfTheMonthBackground.setBackgroundResource(android.R.color.black)
        }
    }

    private fun setUpDaysInCalendar() {
        val auxCalendar = Calendar.getInstance(Locale.getDefault())
        auxCalendar.time = currentCalendar.time
        auxCalendar[Calendar.DAY_OF_MONTH] = 1
        val firstDayOfMonth = auxCalendar[Calendar.DAY_OF_WEEK]
        var dayOfTheMonthText: TextView
        var dayOfTheMonthContainer: ViewGroup
        var dayOfTheMonthLayout: ViewGroup
        // Calculate dayOfTheMonthIndex
        var dayOfTheMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar)
        run {
            var i = 1
            while (i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                dayOfTheMonthContainer =
                    rootView!!.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + dayOfTheMonthIndex)
                dayOfTheMonthText =
                    rootView!!.findViewWithTag(DAY_OF_THE_MONTH_TEXT + dayOfTheMonthIndex)
                dayOfTheMonthContainer.setOnClickListener(onDayOfMonthClickListener)
                // dayOfTheMonthContainer.setOnLongClickListener(onDayOfMonthLongClickListener)
                dayOfTheMonthText.visibility = View.VISIBLE
                dayOfTheMonthText.text = i.toString()
                i++
                dayOfTheMonthIndex++
            }
        }
        for (i in 36..42) {
            dayOfTheMonthText = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_TEXT + i)
            dayOfTheMonthLayout = rootView!!.findViewWithTag(DAY_OF_THE_MONTH_LAYOUT + i)
            if (dayOfTheMonthText.getVisibility() == View.INVISIBLE) {
                dayOfTheMonthLayout.visibility = View.GONE
            } else {
                dayOfTheMonthLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun markDayAsCurrentDay() { // If it's the current month, mark current day
        val nowCalendar = Calendar.getInstance()
        if (nowCalendar[Calendar.YEAR] == currentCalendar[Calendar.YEAR] && nowCalendar[Calendar.MONTH] == currentCalendar[Calendar.MONTH]) {
            val currentCalendar = Calendar.getInstance()
            currentCalendar.time = nowCalendar.time
            val dayOfTheMonthBackground = getDayOfMonthBackground(currentCalendar)
            dayOfTheMonthBackground.setBackgroundResource(R.drawable.ring)
        }
    }

    fun updateView() {
        setUpMonthLayout()
        setUpWeekDaysLayout()
        setUpDaysOfMonthLayout()
        setUpDaysInCalendar()
        markDayAsCurrentDay()
    }


    private fun getDayOfMonthBackground(currentCalendar: Calendar): ViewGroup {
        return getView(DAY_OF_THE_MONTH_BACKGROUND, currentCalendar) as ViewGroup
    }

    private fun getDayOfMonthText(currentCalendar: Calendar): TextView {
        return getView(DAY_OF_THE_MONTH_TEXT, currentCalendar) as TextView
    }

    fun getCircleImage(currentCalendar: Calendar): ImageView {
        return getView(DAY_OF_THE_MONTH_CIRCLE_IMAGE, currentCalendar) as ImageView
    }

    private fun getView(key: String, currentCalendar: Calendar): View {
        val index = getDayIndexByDate(currentCalendar)
        return rootView!!.findViewWithTag(key + index)
    }

    interface CalendarListener {
        fun onDayClick(date: Date?)
        fun onRightButtonClick()
        fun onLeftButtonClick()
    }

    companion object {
        private const val DAY_OF_THE_WEEK_TEXT = "dayOfTheWeekText"
        private const val DAY_OF_THE_WEEK_LAYOUT = "dayOfTheWeekLayout"
        private const val DAY_OF_THE_MONTH_LAYOUT = "dayOfTheMonthLayout"
        private const val DAY_OF_THE_MONTH_TEXT = "dayOfTheMonthText"
        private const val DAY_OF_THE_MONTH_BACKGROUND = "dayOfTheMonthBackground"
        private const val DAY_OF_THE_MONTH_CIRCLE_IMAGE = "dayOfTheMonthCircleImage"
        private fun checkSpecificLocales(
            dayOfTheWeekString: String,
            i: Int
        ): String { // Set Wednesday as "X" in Spanish Locale.getDefault()
            var dayOfTheWeekString = dayOfTheWeekString
            dayOfTheWeekString = if (i == 4 && "RU" == Locale.getDefault().country) {
                "X"
            } else {
                dayOfTheWeekString.substring(0, 1).toUpperCase()
            }
            return dayOfTheWeekString
        }

        private fun getDayIndexByDate(currentCalendar: Calendar): Int {
            val monthOffset = getMonthOffset(currentCalendar)
            val currentDay = currentCalendar[Calendar.DAY_OF_MONTH]
            return currentDay + monthOffset
        }

        private fun getMonthOffset(currentCalendar: Calendar): Int {
            val calendar = Calendar.getInstance()
            calendar.time = currentCalendar.time
            calendar[Calendar.DAY_OF_MONTH] = 1
            val firstDayWeekPosition = calendar.firstDayOfWeek
            val dayPosition = calendar[Calendar.DAY_OF_WEEK]
            return if (firstDayWeekPosition == 1) {
                dayPosition - 1
            } else {
                if (dayPosition == 1) {
                    6
                } else {
                    dayPosition - 2
                }
            }
        }

        private fun getWeekIndex(weekIndex: Int, currentCalendar: Calendar): Int {
            val firstDayWeekPosition = currentCalendar.firstDayOfWeek
            return if (firstDayWeekPosition == 1) {
                weekIndex
            } else {
                if (weekIndex == 1) {
                    7
                } else {
                    weekIndex - 1
                }
            }
        }

        private fun areInTheSameDay(calendarOne: Calendar, calendarTwo: Calendar): Boolean {
            return calendarOne[Calendar.YEAR] == calendarTwo[Calendar.YEAR] && calendarOne[Calendar.DAY_OF_YEAR] == calendarTwo[Calendar.DAY_OF_YEAR]
        }
    }

    fun setHearts(list: ArrayList<Long>) {
        for (i in list) {
            val inputDate = Date.from(Instant.ofEpochMilli(i))
            val inputYear = inputDate.year
            val inputMonth = inputDate.month
            if (currentCalendar.time.year == inputYear && currentCalendar.time.month == inputMonth) {
                currentCalendar.time = inputDate
                val heartImage = this.getCircleImage(currentCalendar)
                heartImage.visibility = View.VISIBLE
            }
        }
    }
}



