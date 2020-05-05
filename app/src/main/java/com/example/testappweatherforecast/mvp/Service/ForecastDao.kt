package com.example.testappweatherforecast.mvp.Service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testappweatherforecast.mvp.entity.ForecastDB

@Dao
interface ForecastDao {
    @Query ("select * from forecast")
    fun getAll(): List<ForecastDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(forecast: ForecastDB)

    @Query("DELETE FROM forecast")
    suspend fun deleteAll()

}