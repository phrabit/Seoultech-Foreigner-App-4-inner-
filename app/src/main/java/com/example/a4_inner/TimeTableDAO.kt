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
}