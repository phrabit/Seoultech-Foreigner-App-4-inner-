package com.example.a4_inner

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TimeTableDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timetable: TimeTable)

    @Delete
    fun delete(timetable: TimeTable)

    @Query("SELECT * FROM Timetable_table")
    fun getAll():List<TimeTable>

    @Query("DELETE FROM Timetable_table")
    fun deleteAll()

    // 수업이름을 기준으로 데이터를 조회하는 메소드 - 중복되어 저장되는 것을 막기위함.
    @Query("SELECT * FROM Timetable_table WHERE class_name = :className")
    fun findByClassName(className: String): TimeTable?

    // 데이터베이스의 전체 데이터 개수를 반환하는 메소드
    @Query("SELECT COUNT(*) FROM Timetable_table")
    fun count(): Int

    @Query("DELETE FROM timetable_table WHERE day_of_week = :dayOfWeek AND period = :period")
    fun deleteByDayAndPeriod(dayOfWeek: String, period: Int)
}