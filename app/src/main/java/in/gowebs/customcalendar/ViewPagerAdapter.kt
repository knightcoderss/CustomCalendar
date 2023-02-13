package `in`.gowebs.customcalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.viewpager.widget.PagerAdapter
import `in`.gowebs.customcalendar.databinding.CalenderLayoutRowBinding

class ViewPagerAdapter(val context: Context, private var mList: ArrayList<AttendanceModel>) : PagerAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int { return mList.size }

    override fun isViewFromObject(view: View, ob: Any): Boolean { return view === ob as RelativeLayout }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = CalenderLayoutRowBinding.inflate(inflater, container, false)
        container.addView(binding.root)

        val adapter = DateAdapter(context, mList[position].data)
        binding.monthRecycler.adapter = adapter
        binding.monthRecycler.layoutManager = GridLayoutManager(context, 7)
        binding.xmlCalenderLayoutRow = mList[position]
        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.monthRecycler)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
        container.removeView(ob as RelativeLayout)
    }
}

