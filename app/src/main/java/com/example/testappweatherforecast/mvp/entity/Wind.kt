package com.example.testappweatherforecast.mvp.entity

import com.google.gson.annotations.SerializedName

data class Wind (

	@SerializedName("speed") val speed : Double,
	@SerializedName("deg") val deg : Double
)