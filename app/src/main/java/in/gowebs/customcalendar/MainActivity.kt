package `in`.gowebs.customcalendar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val calendarView = findViewById<CalendarView>(R.id.calendar_view)
//        calendarView.setBackgroundColor(Color.WHITE)
        val listener = object : OnDaySelectedListener {
            override fun onDaySelected(dayOfMonth: Int) {
                Toast.makeText(this@MainActivity, "Day selected: $dayOfMonth", Toast.LENGTH_SHORT).show()
            }
        }

        val date = "2023-02-10"
        val format = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            time = format.parse(date)!!
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val grid = ArrayList<String>()

        var count = 1
        for (i in 1 until firstDayOfMonth) {
            grid.add("")
        }

        while (count <= maxDaysInMonth) {
            for (i in 1..7) {
                if (count <= maxDaysInMonth) {
                    grid.add(count.toString())
                    count++
                } else {
                    grid.add("")
                }
            }
        }
        Log.e("sdfjs",grid.toString())

// You can now use the grid list to display the calendar in your desired format.

//        calendarView.setOnDaySelectedListener(listener)
//        calendarView.setOnDaySelectedListener { dayOfMonth ->
//            Toast.makeText(this, "Day selected: $dayOfMonth", Toast.LENGTH_SHORT).show()
//        }

    }
}