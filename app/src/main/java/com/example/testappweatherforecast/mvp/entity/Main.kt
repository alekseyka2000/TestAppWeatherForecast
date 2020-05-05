package com.example.testappweatherforecast.mvp.entity

import com.google.gson.annotations.SerializedName

data class Main (

	@SerializedName("temp") val temp : Double = 0.0,
	@SerializedName("temp_min") val temp_min : Double = 0.0,
	@SerializedName("temp_max") val temp_max : Double = 0.0,
	@SerializedName("pressure") val pressure : Double = 0.0,
	@SerializedName("sea_level") val sea_level : Double = 0.0,
	@SerializedName("grnd_level") val grnd_level : Double = 0.0,
	@SerializedName("humidity") val humidity : Int = 0,
	@SerializedName("temp_kf") val temp_kf : Int = 0
)