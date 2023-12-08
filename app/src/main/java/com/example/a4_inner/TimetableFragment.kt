package com.example.a4_inner

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.a4_inner.databinding.FragmentTimetableBinding

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimetableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimetableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentTimetableBinding
    val timeTableDB:TimeTableDB by lazy {TimeTableDB.getInstance(this.requireContext())}

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
        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {
            // 입력할 수 있는 팝업창이 나오도록 하기
            showAddClassDialog()
        }
        return binding.root
    }


    override fun onResume() {
        super.onResume()

        // Shared Preferences에서 강의 정보 불러오기
        val sharedPreferences = requireActivity().getSharedPreferences("Timetable", Context.MODE_PRIVATE)
        for (day in listOf("monday", "tuesday", "wednesday", "thursday", "friday")) {
            for (period in 1..9) {
                val info = sharedPreferences.getString("$day$period", null)
                if (info != null) {
                    val splitInfo = info.split("\n")
                        if (splitInfo.size >= 3) {
                            applyBackgroundColor(day, period, period, splitInfo[0], splitInfo[1], splitInfo[2])
                        }
                }
            }
        }
    }

    private fun showAddClassDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_class, null)

        builder.setView(dialogView)

        val editClassName = dialogView.findViewById<EditText>(R.id.editClassName)
        val spinnerDayOfWeek = dialogView.findViewById<Spinner>(R.id.dayOfWeek)
        val spinnerStartPeriod = dialogView.findViewById<Spinner>(R.id.startPeriod)
        val spinnerEndPeriod = dialogView.findViewById<Spinner>(R.id.endPeriod)
        val spinnerClassRoom = dialogView.findViewById<Spinner>(R.id.spinnerBuilding)
        val editClassroom = dialogView.findViewById<EditText>(R.id.editClassroom)

        // Spinner에 요일 목록 추가
        val dayOfWeekList = arrayOf("Day","monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        val dayOfWeekAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, dayOfWeekList)
        spinnerDayOfWeek.adapter = dayOfWeekAdapter

        // Spinner에 교시 목록 추가
        val periodList = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val periodAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, periodList)
        spinnerStartPeriod.adapter = periodAdapter
        spinnerEndPeriod.adapter = periodAdapter


        // Spinner에 강의실 목록 추가 (이 부분은 프로젝트에 맞게 수정해야 함)
        val classRoomList = arrayOf("Building", "(1) Administration Bldg", "(2) Dasan Hall", "(3) Changhak Hall", "(4) Business Incubation Center 2", "(5) Hyeseong Hall", "(6) Cheongun Hall",
            "(7) Seoul Technopark", "(8) Graduate Schools", "(10) Power Plant", "(11) Bungeobang Pond", "(12) Eoui Stream", "(13) Main Gate", "(14) Ceramics Hall", "(30) SeoulTech Daycare Center",
            "(31) Business Incubation Center", "(32) Frontier Hall", "(33) Hi-Tech Hall", "(34) Central Library", "(35) Central Library Annex", "(36) Suyeon Hall", "(37) Student Union Bldg", "(38) Language Center",
            "(39) Davinci Hall", "(40) Eoui Hall", "(41) Buram Dormitory", "(42) KB Dormitory", "(43) Seongrim Dormitory", "(44) Hyeopdong Gate", "(45) Surim Dormitory", "(46) Nuri Dormitory", "(47) SeoulTech Academy House",
            "(51) The 100th Memorial Hall", "(52) Student Union Bldg. 2", "(53) Sangsang Hall", "(54) Areum Hall", "(55) University Gymnasium", "(56) Daeryuk Hall", "(57) Mugung Hall", "(58) Power Plant 2", "(59) R.O.T.C", "(60) Mirae Hall",
            "(61) Changeui Gate", "(62) Techno Cube", "(63) Main Playground", "(64) South Gate")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classRoomList)
        spinnerClassRoom.adapter = adapter

        builder.setPositiveButton("Add") { dialog, _ ->
            val className = editClassName.text.toString()
            val selectedDayOfWeek = spinnerDayOfWeek.selectedItem.toString()
            val selectedStartPeriod = spinnerStartPeriod.selectedItem.toString()
            val selectedEndPeriod = spinnerEndPeriod.selectedItem.toString()
            val selectedClassRoom = spinnerClassRoom.selectedItem.toString()
            val classroom = editClassroom.text.toString()

            // 강의 정보를 Shared Preferences에 저장
            val sharedPreferences = requireActivity().getSharedPreferences("Timetable", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            for (period in selectedStartPeriod.toInt()..selectedEndPeriod.toInt()) {
                editor.putString("$selectedDayOfWeek$period", "$className\n$selectedClassRoom $classroom")
            }
            editor.apply()

            // TimetableFragment에 메서드를 호출하여 선택된 정보를 이용하여 셀에 색칠
            applyBackgroundColor(selectedDayOfWeek, selectedStartPeriod.toInt(), selectedEndPeriod.toInt(), className, selectedClassRoom, classroom)


            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun applyBackgroundColor(dayOfWeek: String, startPeriod: Int, endPeriod: Int, className: String, selectedClassRoom: String, classRoom: String) {
        // TimetableFragment의 onCreateView에서 Timetable의 각 셀에 해당하는 ID를 찾아 배경색을 변경합니다.
        // 여기서는 간단한 예시로 View를 찾아 직접 배경색 및 텍스트를 변경하도록 하였습니다.
        val rootView = view
        var textFlag = false
        if (rootView != null) {
            for (period in startPeriod..endPeriod) {
                val cellId = "$dayOfWeek$period"
                val cellView = rootView.findViewById<View>(resources.getIdentifier(cellId, "id", requireContext().packageName))

                if (cellView is TextView) {
                    // 해당 셀이 TextView로 캐스팅 가능한 경우에만 텍스트 및 배경색 변경
                    if(!textFlag){
                        cellView.text = "$className\n$selectedClassRoom $classRoom"
                        textFlag = true
                    }

                    cellView.setBackgroundColor(Color.YELLOW) // 여기에서는 노란색으로 설정했습니다. 필요에 따라 수정하세요.
                    cellView.setTextColor(Color.BLUE)

                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimetableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimetableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}