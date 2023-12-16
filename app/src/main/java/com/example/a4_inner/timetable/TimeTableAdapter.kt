package com.example.a4_inner.timetable
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a4_inner.TimeTable
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

            // period 파싱
            val periods = time_table.period.split(" ").map { it.toInt() }

            // 해당 시간대에 대한 TextView 찾기
            for (i in periods[0]..periods[1]) {
                val textViewId = binding.root.resources.getIdentifier("${time_table.day}$i", "id", binding.root.context.packageName)
                val textView = binding.root.findViewById<TextView>(textViewId)

                // TextView에 수업 정보 설정
                textView.text = "${time_table.className}\n${time_table.selectedClassRoom}\n${time_table.classroom}"

                textView.setBackgroundColor(Color.RED)
                textView.setTextColor(Color.WHITE)
            }
        }
    }
}