package `in`.gowebs.customcalendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
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
        aList.add(getMonth(currentItem))
        if (aList.size == 1) {
            aList.add(getMonth(currentItem + 1))
        }
        viewPagerAdapter.notifyDataSetChanged()
        val selectedCountTV: TextView = findViewById(R.id.selected_count_tv)


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
                if (position > lastPosition) {
                    lastPosition = position
                    aList.add(getMonth(currentItem + 1))
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

            }
        }
    }

    private fun getMonth(currentItem: Int): AttendanceModel {
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


    private fun populateDays(
        maxDaysInMonth: Int,
        firstDayOfMonth: Int,
    ): ArrayList<AttendanceModel.DayEvent> {
        var count = 0
        var date = 1
        val mList = ArrayList<AttendanceModel.DayEvent>()
        while (count < maxDaysInMonth + firstDayOfMonth) {
            if (firstDayOfMonth > count) {
                mList.add(count, AttendanceModel.DayEvent())
            } else {
                val day = if ((date) < 10) "0${date}" else "$date"
                mList.add(AttendanceModel.DayEvent(day))
                date++
            }
            count++
        }
        return mList
    }
}