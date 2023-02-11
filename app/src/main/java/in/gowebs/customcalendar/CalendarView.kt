package `in`.gowebs.customcalendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

class CalendarView : View {
    private var mMonth: Int = 0
    private var mYear: Int = 0
    private var mDay: Int = 0
    private var mToday: Calendar = Calendar.getInstance()
    private var mCalendar: Calendar = Calendar.getInstance()
    private var mPaint: Paint = Paint()
    private var selectedDay = -1
    private var onDaySelectedListener: OnDaySelectedListener? = null
    fun setOnDaySelectedListener(listener: OnDaySelectedListener?) { onDaySelectedListener = listener }
    constructor(context: Context?) : super(context) { init() }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init() }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }
    private fun init() {
        mToday.time = Date()
        mMonth = mToday.get(Calendar.MONTH)
        mYear = mToday.get(Calendar.YEAR)
        mDay = mToday.get(Calendar.DAY_OF_MONTH)
        mCalendar.time = mToday.time
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val cellWidth = width / 7f
        val cellHeight = height / 7f
        mPaint.color = Color.BLACK
        mPaint.textSize = 30f
        val monthName = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val monthWidth = mPaint.measureText(monthName)
        canvas?.drawText(monthName, (width / 2 - monthWidth / 2).toFloat(), (height / 12).toFloat(), mPaint)
        val year = mCalendar.get(Calendar.YEAR)
        val yearString = year.toString()
        val yearWidth = mPaint.measureText(yearString)
        canvas?.drawText(yearString, (width / 2 - yearWidth / 2).toFloat(), (height / 12 * 2).toFloat(), mPaint)
        mPaint.textSize = 25f
        val daysOfWeek = arrayOf("S", "M", "T", "W", "T", "F", "S")
        for (i in daysOfWeek.indices) {
            val dayWidth = mPaint.measureText(daysOfWeek[i])
            canvas?.drawText(daysOfWeek[i], ((i + 1) * width / 8 - dayWidth / 2).toFloat(), (height / 12 * 3).toFloat(), mPaint)
        }
        val firstDay = mCalendar.get(Calendar.DAY_OF_WEEK)
        mCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val numberOfDays = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var row = 5
        for (day in 1..numberOfDays) {
            val column = (day + firstDay - 2) % 7
            val dayString = day.toString()
            val dayWidth = mPaint.measureText(dayString)
            canvas?.drawText(
                dayString,
                ((column + 1) * width / 8 - dayWidth / 2).toFloat(),
                (row * height / 12).toFloat(),
                mPaint
            )
            if (day % 7 == 0) { row++ }
        }
        if (selectedDay != -1) {
            val selectedDayPaint = Paint().apply {
                color = Color.RED
                isAntiAlias = true
                textSize = 20f
            }
            val x = (selectedDay % 7) * cellWidth + cellWidth / 2
            val y = (selectedDay / 7) * cellHeight + cellHeight / 2
            canvas?.drawText("$selectedDay", x, y, selectedDayPaint)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val x = event.x
            val y = event.y
            selectedDay = getSelectedDayFromCoordinates(x, y)
            invalidate()
        }
        return true
    }
    private fun getSelectedDayFromCoordinates(x: Float, y: Float): Int {
        val width = width.toFloat()
        val height = height.toFloat()
        val cellWidth = width / 7
        val cellHeight = height / 7
        val column = (x / cellWidth).toInt()
        val row = (y / cellHeight).toInt()
        return row * 7 + column + 1
    }
}