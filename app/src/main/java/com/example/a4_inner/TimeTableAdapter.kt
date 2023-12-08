package com.example.a4_inner
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4_inner.databinding.FragmentTimetableBinding

class TimeTableAdapter(val timetable_list:MutableList<TimeTable>):RecyclerView.Adapter<TimeTableAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time_table = timetable_list.get(position)
        holder.bind(time_table)
    }

    override fun getItemCount(): Int {
        return timetable_list.size
    }

    class ViewHolder(val binding:FragmentTimetableBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(time_table : TimeTable){
            //여기서 사용
            //Time table ui component 불러서 사용

        }
    }
}
