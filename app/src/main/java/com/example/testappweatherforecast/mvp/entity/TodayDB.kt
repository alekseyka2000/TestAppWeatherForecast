package com.example.testappweatherforecast.mvp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "today")
class TodayDB {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @ColumnInfo(name = "icon") var icon : String = ""
    @ColumnInfo(name = "city") var city : String = ""
    @ColumnInfo(name = "country") var country : String = ""
    @ColumnInfo(name = "temp") var temp : Double = 0.0
    @ColumnInfo(name = "description") var description : String = ""
    @ColumnInfo(name = "humidity") var humidity : Int = 0
    @ColumnInfo(name = "pressure") var pressure : Double = 0.0
    @ColumnInfo(name = "speed") var speed : Double = 0.0
    @ColumnInfo(name = "deg") var deg : Double = 0.0
    @ColumnInfo(name = "dt_txt") var dtTxt : String = ""
    @ColumnInfo(name = "main") var main : String = ""
}