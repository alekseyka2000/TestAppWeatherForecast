package com.example.testappweatherforecast.mvp.entity

import com.google.gson.annotations.SerializedName

data class City (

	@SerializedName("id") val id : Int = 0,
	@SerializedName("name") val name : String = "",
	@SerializedName("coord") val coord : Coord = Coord(),
	@SerializedName("country") val country : String = ""
)