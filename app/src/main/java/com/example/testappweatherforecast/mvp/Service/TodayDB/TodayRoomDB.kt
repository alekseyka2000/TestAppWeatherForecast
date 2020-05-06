package com.example.testappweatherforecast.mvp.Service.TodayDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testappweatherforecast.mvp.entity.TodayDB

@Database(entities = arrayOf(TodayDB::class), version = 1, exportSchema = false)
abstract class TodayRoomDB: RoomDatabase() {

    abstract fun todayDao(): TodayDao

    companion object {
        @Volatile
        private var INSTANCE: TodayRoomDB? = null

        fun getDatabase(context: Context): TodayRoomDB {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodayRoomDB::class.java,
                    "today_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}