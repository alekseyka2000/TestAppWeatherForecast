package com.example.testappweatherforecast.mvp.Service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testappweatherforecast.mvp.entity.ForecastDB

@Database(entities = arrayOf(ForecastDB::class), version = 1, exportSchema = false)
public abstract class ForecastRoomDB: RoomDatabase() {

    abstract fun forecastDao(): ForecastDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ForecastRoomDB? = null

        fun getDatabase(context: Context): ForecastRoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForecastRoomDB::class.java,
                    "forecast_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}
