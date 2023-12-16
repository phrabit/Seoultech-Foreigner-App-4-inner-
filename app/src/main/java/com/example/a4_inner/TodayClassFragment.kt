package com.example.a4_inner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import com.example.a4_inner.databinding.FragmentRecentDestinationBinding
import com.example.a4_inner.databinding.FragmentTodayClassBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG_TODAY_CLASS = "today_class_fragment"
/**
 * A simple [Fragment] subclass.
 * Use the [TodayClassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodayClassFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentTodayClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayClassBinding.inflate(inflater, container, false)
        getTodayClassData()
        binding.todayClass1Txt.visibility = View.INVISIBLE
        binding.todayClass2Txt.visibility = View.INVISIBLE
        binding.todayClass3Txt.visibility = View.INVISIBLE
        return binding.root
    }

    public fun getTodayClassData(){
        loadTimetableDataForToday(this)
    }
    public fun refresh(){
        val lecture_txt_list = arrayListOf(binding.todayClass1Txt, binding.todayClass2Txt, binding.todayClass3Txt)
        val today_class_list = todayTimeTable.getTodayTimetable()
        if(today_class_list != null || today_class_list?.size == 0) {
            binding.noClassTxt.visibility = View.INVISIBLE
            Log.d("ITM", "today class exists")
            for((lecture,ui) in today_class_list.zip(lecture_txt_list)){
                ui.text = lecture.className
                ui.visibility = View.VISIBLE
                ui.setOnClickListener {
                    showDialog(lecture)
                }
            }
        }
        else{
            Log.d("ITM", "today class not exists")
            binding.noClassTxt.visibility = View.VISIBLE
            for(ui in lecture_txt_list){
                ui.visibility = View.INVISIBLE
            }
        }
    }
    fun showDialog(lecture : TimetableItem){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Class Info")
            .setMessage("Class name: ${lecture.className}\n" +
                    "Class day: ${lecture.day}\n" +
                    "Class time: ${lecture.startPeriod} ~ ${lecture.endPeriod}\n"+
                    "Class location: ${lecture.classroom}\n" +
                    "Class room: ${lecture.selectedClassRoom}\n")
            .create()
            .show()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodayClassFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayClassFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}