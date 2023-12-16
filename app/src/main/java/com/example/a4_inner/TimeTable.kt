package com.example.a4_inner

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.a4_inner.fragments.TodayClassFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity(tableName = "Timetable_table")
data class TimeTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "day_of_week") val day: String,
    @ColumnInfo(name = "period") val period: String,
    @ColumnInfo(name = "class_name") val className: String,
    @ColumnInfo(name = "building") val selectedClassRoom: String,
    @ColumnInfo(name = "class_room") val classroom: String
)

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

fun loadTimetableDataForToday(today_class_fragment : TodayClassFragment?) {
    val currentDate = getCurrentDate()
    val currentDay = getDayOfWeek(currentDate)

    // 특정 요일의 시간표 정보를 가져옵니다.
    loadTimetableDataForDay(currentDay, today_class_fragment){
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

        if(today_class_fragment != null){
            today_class_fragment.refresh()
        }
    }
}

fun loadTimetableDataForDay(day: String, today_class_fragment : TodayClassFragment?,  callback: () -> Unit) {
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
                    today_class_fragment?.refresh()
                }
            } else {
                Log.d("ITM", "해당 문서가 없습니다")
                today_class_fragment?.refresh()
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