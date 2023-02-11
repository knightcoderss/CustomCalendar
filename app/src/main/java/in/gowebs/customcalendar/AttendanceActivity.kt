package `in`.gowebs.customcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import androidx.viewpager.widget.ViewPager
import `in`.gowebs.customcalendar.databinding.ActivityAttendanceBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.max

class AttendanceActivity : AppCompatActivity() {
    private lateinit var databinding: ActivityAttendanceBinding
    private var currentItem = 0
    private val aList = ArrayList<AttendanceModel>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var aListRemoveAt = true
    private val minScale = 0.75f
    private var lastPosition = 0
    var attendanceModel = AttendanceModel(ArrayList())

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_attendance)
        initializeUI()
//        setMonth(attendanceModel)
        getMonth()

    }

    private fun initializeUI() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        databinding.backBtnTv.setOnClickListener { finish() }
        viewPagerAdapter = ViewPagerAdapter(this, aList)
        databinding.productDetailsViewpager.adapter = viewPagerAdapter
        databinding.productDetailsViewpager.offscreenPageLimit = 1
        databinding.productDetailsViewpager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
                if (position >= lastPosition) {
                    lastPosition = position
                    setMonth(attendanceModel)
                }
                if (aListRemoveAt) {
                    aListRemoveAt = false
                    aList.removeAt(0)
                    viewPagerAdapter.notifyDataSetChanged()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        databinding.productDetailsViewpager.setPageTransformer(false) { page, position ->
            val scaleFactor = max(minScale, 1 - abs(position))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            if (abs(position) < 0.01f) {
                Toast.makeText(this@AttendanceActivity, "$position", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setMonth(data: AttendanceModel) {
        val calendar = getCalendarWithCurrentMonthMinusPosition(currentItem)
        val currentMonthYear = getCurrentMonthYearString(calendar)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        populateDays(data, maxDaysInMonth, firstDayOfMonth)
        aList.add(AttendanceModel(data = data.data, monthName = "$currentMonthYear ${calendar.get(Calendar.YEAR)} "))
        if (aList.size == 1) { aList.add(0, AttendanceModel(data = data.data, monthName = "$currentMonthYear ${calendar.get(Calendar.YEAR)} ")) }
        viewPagerAdapter.notifyDataSetChanged()
    }

    private fun getMonth(): AttendanceModel {
        val calendar = getCalendarWithCurrentMonthMinusPosition(currentItem)
        val currentMonthYear = getCurrentMonthYearString(calendar)
        val currentYear = calendar.get(Calendar.YEAR)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val mList = populateDays(maxDaysInMonth, firstDayOfMonth)
        return AttendanceModel(data = mList, monthName = "$currentMonthYear $currentYear ")
    }

    private fun getCalendarWithCurrentMonthMinusPosition(position: Int): Calendar {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        val format = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = format.parse(formattedDate)!!
            set(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.add(Calendar.MONTH, -position)
        return calendar
    }

    private fun getCurrentMonthYearString(calendar: Calendar): String {
        return SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time).substring(0, 3)
            .uppercase(Locale.getDefault())
    }

    private fun populateDays(data: AttendanceModel, maxDaysInMonth: Int, firstDayOfMonth: Int) {
        var count = 0
        var count2 = 1
        while (count < maxDaysInMonth + firstDayOfMonth) {
            if (firstDayOfMonth > count) {
                data.data.add(count, AttendanceModel.DayEvent())
            } else {
                val day = if (count2 < 10) "0$count2" else "$count2"
                if (data.data.size <= count) {
                    data.data.add(AttendanceModel.DayEvent(day))
                } else {
                    data.data[count].day = day
                }
                count2++
            }
            count++
        }
    }

    private fun populateDays(
        maxDaysInMonth: Int,
        firstDayOfMonth: Int,
    ): ArrayList<AttendanceModel.DayEvent> {
        var count = 0
        val mList = ArrayList<AttendanceModel.DayEvent>()
        while (count < maxDaysInMonth + firstDayOfMonth) {
            if (firstDayOfMonth > count) {
                mList.add(count, AttendanceModel.DayEvent())
            } else {
                val day = if ((count + 1) < 10) "0${count + 1}" else "${count + 1}"
                mList.add(AttendanceModel.DayEvent(day))
            }
            count++
        }
        return mList
    }
}