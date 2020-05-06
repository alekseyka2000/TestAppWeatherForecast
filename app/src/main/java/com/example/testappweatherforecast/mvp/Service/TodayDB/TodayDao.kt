package com.example.testappweatherforecast.mvp.Service.TodayDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testappweatherforecast.mvp.entity.TodayDB


@Dao
interface TodayDao {
    @Query("select * from today")
    fun getAll(): List<TodayDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(today: TodayDB)

    @Query("DELETE FROM today")
    suspend fun deleteAll()

}