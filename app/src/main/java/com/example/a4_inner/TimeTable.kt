package com.example.a4_inner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Timetable_table")
data class TimeTable(
    @PrimaryKey(autoGenerate = false) val  weekday:String,
    @ColumnInfo(name="class1_name") val class1_name:String,
    @ColumnInfo(name="class1_time") val class1_time: String,
    @ColumnInfo(name="class1_building") val class1_building: String,
    @ColumnInfo(name="class1_room") val class1_room: String,

    @ColumnInfo(name="class2_name") val class2_name:String,
    @ColumnInfo(name="class2_time") val class2_time: String,
    @ColumnInfo(name="class2_building") val class2_building: String,
    @ColumnInfo(name="class2_room") val class2_room: String,

    @ColumnInfo(name="class3_name") val class3_name:String,
    @ColumnInfo(name="class3_time") val class3_time: String,
    @ColumnInfo(name="class3_building") val class3_building: String,
    @ColumnInfo(name="class3_room") val class3_room: String,

    @ColumnInfo(name="class4_name") val class4_name:String,
    @ColumnInfo(name="class4_time") val class4_time: String,
    @ColumnInfo(name="class4_building") val class4_building: String,
    @ColumnInfo(name="class4_room") val class4_room: String,
)