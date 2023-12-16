package com.example.a4_inner

import android.app.AlertDialog
import android.content.Context
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
import com.example.a4_inner.databinding.FragmentTimetableBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.checkerframework.checker.units.qual.Current
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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


        loadTimetableDataForToday()
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

        timetableRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.data!=null) {
                    Log.d("ITM", "DocumentSnapshot data: ${document.data}")
                    Log.d("ITM","hello?")

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
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////

    // 남섭이형을 위한 코드
    // getCurrentDate(), getDayOfWeek(currentDate)로 오늘 날짜와 요일을 구함
    // loadTimetableForToday()로 오늘 요일의 해당하는 시간표 정보를 가져옴.

    object todayTimeTable {
        private var todayTimetable: List<TimetableItem>? = null

        fun setTodayTimetable(timetable: List<TimetableItem>) {
            todayTimetable = timetable
        }

        fun getTodayTimetable(): List<TimetableItem>? {
            return todayTimetable
        }
    }

    data class TimetableItem(
        val day: String,
        val startPeriod: Int,
        val endPeriod: Int,
        val className: String,
        val selectedClassRoom: String,
        val classroom: String
    )

    fun loadTimetableDataForToday() {
        val currentDate = getCurrentDate()
        val currentDay = getDayOfWeek(currentDate)

        // 특정 요일의 시간표 정보를 가져옵니다.
        loadTimetableDataForDay(currentDay){
            Log.d("ITM","Callback is called.")

            val todayTimetable = todayTimeTable.getTodayTimetable()
            Log.d("ITM", "Today's Timetable: $todayTimetable")

            // 추가된 Log 문
            Log.d("ITM", "Today's Timetable Size: ${todayTimetable?.size}")

            // 오늘의 시간표를 출력합니다.
            Log.d("ITM", "Today's Timetable: $todayTimetable")
            Log.d("ITM", "Today's Timetable Size: ${todayTimetable?.size}")

            if (todayTimetable == null) {
                Log.d("ITM", "Today's Timetable is null")
            } else {
                // 여기에서 오늘의 시간표 데이터를 사용할 수 있습니다.
                // 예를 들어, 시간표의 첫 번째 항목의 이름을 출력하거나 할 수 있습니다.
                Log.d("ITM", "First class of today: ${todayTimetable[0].className}")
            }
        }
    }

    fun loadTimetableDataForDay(day: String, callback: () -> Unit) {
        val userId = CurrentUser.getUserUid
        val timetableRef = FirebaseFirestore.getInstance()
            .collection("Timetable")
            .document(userId.toString())

        timetableRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.data != null) {
                    val timetableInfo = document.data as Map<String, Any>
                    val dayInfo = timetableInfo[day] as? Map<String, Any>

                    if (dayInfo != null) {
                        val todayTimetable = mutableListOf<TimetableItem>()

                        for ((_, classInfo) in dayInfo) {
                            val classInfoDetails = classInfo as Map<String, List<String>>
                            for ((_, classInfo) in classInfoDetails) {
                                val period = classInfo[0]
                                val className = classInfo[1]
                                val selectedClassRoom = classInfo[2]
                                val classroom = classInfo[3]

                                // TimetableItem을 생성하고 목록에 추가합니다.
                                val timetableItem = TimetableItem(
                                    day = day,
                                    startPeriod = period.split(" ")[0].toInt(),
                                    endPeriod = period.split(" ")[1].toInt(),
                                    className = className,
                                    selectedClassRoom = selectedClassRoom,
                                    classroom = classroom
                                )
                                todayTimetable.add(timetableItem)
                            }
                        }

                        // 싱글톤 객체에 오늘의 시간표를 설정합니다.
                        todayTimeTable.setTodayTimetable(todayTimetable)
                        Log.d("ITM","오늘의 시간표 : $todayTimetable")

                        callback.invoke()

                        // 필요하다면 여기에서 앱에 데이터를 적용할 수도 있습니다.
                    } else {
                        Log.d("ITM", "해당 날짜의 시간표 데이터가 없습니다: $day")
                    }
                } else {
                    Log.d("ITM", "해당 문서가 없습니다")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ITM", "데이터 가져오기 실패: ", exception)
            }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getDayOfWeek(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        val cal = Calendar.getInstance()
        cal.time = parsedDate ?: Date()
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        // Calendar.DAY_OF_WEEK의 값은 일요일(1)부터 토요일(7)까지입니다.
        // Firestore의 문서에 따라 월요일부터 일요일까지의 순서에 맞게 조정합니다.
        val firestoreDayOfWeek = when (dayOfWeek) {
            Calendar.SUNDAY -> 7
            else -> dayOfWeek - 1
        }

        return when (firestoreDayOfWeek) {
            1 -> "monday"
            2 -> "tuesday"
            3 -> "wednesday"
            4 -> "thursday"
            5 -> "friday"
            6 -> "saturday"
            7 -> "sunday"
            else -> throw IllegalArgumentException("Invalid day of week")
        }
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////




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
            "(7) Seoul Technopark", "(8) Graduate Schools", "(10) Power Plant", "(11) Bungeobang Pond", "(13) Main Gate", "(30) SeoulTech Daycare Center",
            "(32) Frontier Hall", "(33) Hi-Tech Hall", "(34) Central Library", "(35) Central Library Annex", "(37) Student Union Bldg", "(38) Language Center",
            "(39) Davinci Hall", "(40) Eoui Hall", "(41) Buram Dormitory", "(42) KB Dormitory", "(43) Seongrim Dormitory", "(44) Hyeopdong Gate", "(45) Surim Dormitory", "(46) Nuri Dormitory",
            "(51) The 100th Memorial Hall", "(52) Student Union Bldg. 2", "(53) Sangsang Hall", "(54) Areum Hall", "(55) University Gymnasium", "(56) Daeryuk Hall", "(57) Mugung Hall", "(58) Power Plant 2", "(60) Mirae Hall",
            "(61) Changeui Gate", "(62) Techno Cube", "(63) Main Playground")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classRoomList)
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

            timetableRef.set(timetableInfo, SetOptions.merge())

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

                    cellView.setBackgroundColor(Color.RED)
                    cellView.setTextColor(Color.WHITE)

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