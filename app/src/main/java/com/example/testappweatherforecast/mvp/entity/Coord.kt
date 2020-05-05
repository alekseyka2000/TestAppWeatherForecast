package com.example.testappweatherforecast.mvp.entity

import com.google.gson.annotations.SerializedName

data class Coord (

	@SerializedName("lat") val lat : Double = 0.0,
	@SerializedName("lon") val lon : Double = 0.0
)