package com.example.testappweatherforecast.mvp.entity

import com.google.gson.annotations.SerializedName

data class WeatherList(

	@SerializedName("dt") val dt : Int = 0,
	@SerializedName("main") val main : Main = Main(),
	@SerializedName("weather") val weather : List<Weather> = listOf(Weather()),
	@SerializedName("clouds") val clouds : Clouds = Clouds(),
	@SerializedName("wind") val wind : Wind = Wind(),
	@SerializedName("rain") val rain : Rain = Rain(),
	@SerializedName("sys") val sys : Sys = Sys(),
	@SerializedName("dt_txt") val dt_txt : String = ""
)