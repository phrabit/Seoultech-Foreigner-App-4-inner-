package com.example.a4_inner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Timetable_table")
data class TimeTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "day_of_week") val day: String,
    @ColumnInfo(name = "period") val period: String,
    @ColumnInfo(name = "class_name") val className: String,
    @ColumnInfo(name = "building") val selectedClassRoom: String,
    @ColumnInfo(name = "class_room") val classroom: String
)