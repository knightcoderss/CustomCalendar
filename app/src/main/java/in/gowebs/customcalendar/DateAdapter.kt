package `in`.gowebs.customcalendar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import `in`.gowebs.customcalendar.databinding.DaysItemsBinding

class DateAdapter(val context: Context, private val mList: ArrayList<AttendanceModel.DayEvent>) :
    RecyclerView.Adapter<DateAdapter.InnerItemViewHolder>() {

    var selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DaysItemsBinding.inflate(inflater, parent, false)
        return InnerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InnerItemViewHolder, position: Int) {
        holder.bind(mList[position], selectedPositions.contains(position))
        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                holder.onItemClear()
            } else {
                selectedPositions.add(position)
                holder.onItemSelected()
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class InnerItemViewHolder(val binding: DaysItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onItemSelected() {
            itemView.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.red_circle_background)
            binding.oneMFirstRowTv.setTextColor(Color.WHITE)
        }

        fun onItemClear() {
            itemView.background = null
            binding.oneMFirstRowTv.setTextColor(ContextCompat.getColor(binding.oneMFirstRowTv.context, R.color.black))
        }
        fun bind(day: AttendanceModel.DayEvent, contains: Boolean) {
            binding.xmlCalenderLayoutRow2 = day
            binding.executePendingBindings()
            if ((adapterPosition) % 7 == 0) {
                binding.oneMFirstRowTv.setTextColor(
                    ContextCompat.getColor(
                        binding.oneMFirstRowTv.context,
                        android.R.color.holo_red_light
                    )
                )
            } else {
                binding.oneMFirstRowTv.setTextColor(
                    ContextCompat.getColor(
                        binding.oneMFirstRowTv.context,
                        R.color.black
                    )
                )
            }
            when (day.Status) {
                "" -> {
                    binding.presentType.visibility = View.GONE
                }
                "P" -> {
                    binding.presentType.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                    binding.presentType.visibility = View.VISIBLE
                }
                "P/2" -> {
                    binding.presentType.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow))
                    binding.presentType.visibility = View.VISIBLE
                }
                "LE" -> {
                    binding.presentType.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
                    binding.presentType.visibility = View.VISIBLE
                }
                "LA" -> {
                    binding.presentType.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue))
                    binding.presentType.visibility = View.VISIBLE
                }
            }
            binding.root.setOnClickListener {
                if (day.day != "") {
                    Toast.makeText(context, day.day, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
