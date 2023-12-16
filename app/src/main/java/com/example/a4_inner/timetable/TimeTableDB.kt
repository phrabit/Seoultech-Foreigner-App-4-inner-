package com.example.a4_inner.timetable
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a4_inner.TimeTable

@Database(entities = [TimeTable::class], version = 1)
abstract class TimeTableDB : RoomDatabase(){
    abstract fun timetableDAO(): TimeTableDAO

    companion object{
        @Volatile
        private var INSTANCE: TimeTableDB? = null

        fun getInstance(context: Context): TimeTableDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeTableDB::class.java,
                    "timetable_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}