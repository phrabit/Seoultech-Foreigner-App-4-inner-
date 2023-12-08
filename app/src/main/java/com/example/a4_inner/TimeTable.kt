package com.example.a4_inner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Lotto_numbers")
data class TimeTable(
    @PrimaryKey(autoGenerate = false) val  weekday:String,
    @ColumnInfo(name="class1") val class1:String,
)