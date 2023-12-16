package com.example.a4_inner.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.a4_inner.CurrentUser
import com.example.a4_inner.FireBase
import com.example.a4_inner.FragmentTags
import com.example.a4_inner.R
import com.example.a4_inner.TimeTable
import com.example.a4_inner.UniversitySites
import com.example.a4_inner.timetable.TimeTableDB
import com.example.a4_inner.activities.NaviActivity
import com.example.a4_inner.databinding.FragmentTimetableBinding
import com.example.a4_inner.loadTimetableDataForToday
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
    val timeTableDB: TimeTableDB by lazy { TimeTableDB.getInstance(this.requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        loadTimetableDataForToday(null)
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
        loadTimetableData()
    }
    fun loadTimetableData() {
        val userId = CurrentUser.getUserUid
        val timetableRef = FirebaseFirestore.getInstance()
            .collection("Timetable")
            .document(userId.toString())

        // 1. Firestore의 flag 변수 확인
        if (FireBase.firebase_online!!) { // flag가 True인 경우
            // Firestore에서 데이터를 불러옵니다.
            timetableRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.data != null) {
                        Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                        Log.d("ITM", "hello?")

                        val timetableInfo = document.data as Map<String, Any>

                        // RoomDB 작업
                        val timetableDao = TimeTableDB.getInstance(requireContext()).timetableDAO()

                        // 데이터를 불러올 때마다 기존 데이터를 삭제합니다.
                        timetableDao.deleteAll()

                        for ((day, info) in timetableInfo) {
                            val dayInfo = info as Map<String, Any>
                            for ((_, classInfo) in dayInfo) {
                                val classInfoDetails = classInfo as Map<String, List<String>>
                                for ((_, classInfo) in classInfoDetails) {
                                    val period = classInfo[0]
                                    val className = classInfo[1]
                                    val selectedClassRoom = classInfo[2]
                                    val classroom = classInfo[3]

                                    // Apply the loaded data to your app
                                    applyBackgroundColor(day, period.split(" ")[0].toInt(), period.split(" ")[1].toInt(), className, selectedClassRoom, classroom)

                                    // 데이터를 Room에 저장
                                    timetableDao.insert(TimeTable(0, day, period, className, selectedClassRoom, classroom))
                                }
                            }
                        }

                        // 삽입 후 모든 데이터 가져오기
                        val allData = timetableDao.getAll()
                        allData.forEach { data ->
                            Log.d("RoomDB", data.toString())
                        }

                    } else {
                        Log.d("ITM", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ITM", "get failed with ", exception)
                }
        } else { // flag가 False인 경우
            // RoomDB에서 데이터를 불러옵니다.
            val timetableDao = TimeTableDB.getInstance(requireContext()).timetableDAO()
            val allData = timetableDao.getAll()

            for (data in allData) {
                applyBackgroundColor(data.day, data.period.split(" ")[0].toInt(), data.period.split(" ")[1].toInt(), data.className, data.selectedClassRoom, data.classroom)
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
        val dayOfWeekList = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday")
        val dayOfWeekAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, dayOfWeekList)
        spinnerDayOfWeek.adapter = dayOfWeekAdapter

        // Spinner에 교시 목록 추가
        val periodList = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9")
        val periodAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            periodList
        )
        spinnerStartPeriod.adapter = periodAdapter
        spinnerEndPeriod.adapter = periodAdapter


        // Spinner에 강의실 목록 추가 (이 부분은 프로젝트에 맞게 수정해야 함)
        val classRoomList = UniversitySites.building_array

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            classRoomList
        )
        spinnerClassRoom.adapter = adapter

        builder.setPositiveButton("Add") { dialog, _ ->
            val className = editClassName.text.toString()
            val selectedDayOfWeek = spinnerDayOfWeek.selectedItem.toString()
            val selectedStartPeriod = spinnerStartPeriod.selectedItem.toString()
            val selectedEndPeriod = spinnerEndPeriod.selectedItem.toString()
            val selectedClassRoom = spinnerClassRoom.selectedItem.toString()
            val classroom = editClassroom.text.toString()

            // 강의 정보를 Firebase에 저장
            val userId = CurrentUser.getUserUid
            val timetableRef = FirebaseFirestore.getInstance()
                .collection("Timetable")
                .document(userId.toString())

            val timetableInfo = mapOf(
                selectedDayOfWeek to mapOf(
                    "0" to mapOf(
                        "$selectedStartPeriod" to listOf(
                            "$selectedStartPeriod $selectedEndPeriod",
                            className,
                            selectedClassRoom,
                            classroom
                        )
                    )
                )
            )

            val job = timetableRef.set(timetableInfo, SetOptions.merge()).addOnCompleteListener {
                ((requireActivity() as? NaviActivity)!!.supportFragmentManager.findFragmentByTag(
                    FragmentTags.TAG_HOME
                ) as? HomeFragment)?.refresh()
                Log.d("ITM", "job complete!")
            }

            // TimetableFragment에 메서드를 호출하여 선택된 정보를 이용하여 셀에 색칠
            applyBackgroundColor(
                selectedDayOfWeek,
                selectedStartPeriod.toInt(),
                selectedEndPeriod.toInt(),
                className,
                selectedClassRoom,
                classroom
            )

            dialog.dismiss()

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun applyBackgroundColor(
        dayOfWeek: String,
        startPeriod: Int,
        endPeriod: Int,
        className: String,
        selectedClassRoom: String,
        classRoom: String
    ) {
        // TimetableFragment의 onCreateView에서 Timetable의 각 셀에 해당하는 ID를 찾아 배경색을 변경합니다.
        // 여기서는 간단한 예시로 View를 찾아 직접 배경색 및 텍스트를 변경하도록 하였습니다.
        val rootView = view
        var textFlag = false
        if (rootView != null) {
            for (period in startPeriod..endPeriod) {
                val cellId = "$dayOfWeek$period"
                val cellView = rootView.findViewById<View>(
                    resources.getIdentifier(
                        cellId,
                        "id",
                        requireContext().packageName
                    )
                )

                if (cellView is TextView) {
                    // 해당 셀이 TextView로 캐스팅 가능한 경우에만 텍스트 및 배경색 변경
                    if (!textFlag) {
                        cellView.text = "$className\n$selectedClassRoom $classRoom"
                        textFlag = true
                    }

                    cellView.setOnClickListener {
                        showDeleteConfirmationDialog(dayOfWeek, startPeriod, endPeriod)
                    }

                    cellView.setBackgroundColor(Color.RED)
                    cellView.setTextColor(Color.WHITE)

                }
            }
        }
    }

    // 배경색을 원래 색상으로 되돌리는 함수
    private fun revertBackgroundColor(dayOfWeek: String, startPeriod: Int, endPeriod: Int, rootView: View?) {
        if (rootView != null) {
            for (p in startPeriod..endPeriod) {
                val cellId = "$dayOfWeek$p"
                val cellView = rootView.findViewById<View>(resources.getIdentifier(cellId, "id", requireContext().packageName))

                if (cellView is TextView) {
                    // 여기에서는 원래 색상을 사용자가 원하는 색상으로 변경해야 합니다.
                    // 사용자가 지정한 배경색의 리소스 ID를 사용하여 배경색을 설정합니다.
                    val originalBackgroundDrawable = ContextCompat.getDrawable(requireContext(),
                        R.drawable.cell_retrieve
                    )
                    cellView.background = originalBackgroundDrawable

                    cellView.setTextColor(Color.BLACK) // 텍스트 색상도 원래대로 변경해주세요

                    // TextView의 텍스트 내용 삭제
                    cellView.text = ""
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(dayOfWeek: String, startPeriod: Int, endPeriod: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Delete?")
        alertDialogBuilder.setMessage("Do you want to delete??")

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            // Room Database에서도 해당 항목 삭제
            revertBackgroundColor(dayOfWeek, startPeriod, endPeriod, view)

            // Firestore에서 해당 항목 삭제
            deleteTimetableEntry(dayOfWeek, startPeriod, endPeriod)
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }

    private fun deleteTimetableEntry(dayOfWeek: String, startPeriod: Int, endPeriod: Int) {
        val userId = CurrentUser.getUserUid
        val timetableRef = FirebaseFirestore.getInstance()
            .collection("Timetable")
            .document(userId.toString())

        timetableRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.data!=null) {
                    val timetableInfo = document.data as MutableMap<String, Any>
                    val dayInfo = timetableInfo[dayOfWeek] as MutableMap<String, Any>

                    val periodKeyToRemove = dayInfo.keys.find { key ->
                        val classInfo = dayInfo[key] as Map<String, List<String>>
                        val period = classInfo.values.first()[0]
                        val start = period.split(" ")[0].toInt()
                        val end = period.split(" ")[1].toInt()
                        startPeriod == start && endPeriod == end
                    }

                    if (periodKeyToRemove != null) {
                        dayInfo.remove(periodKeyToRemove)
                        timetableRef.set(timetableInfo)
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