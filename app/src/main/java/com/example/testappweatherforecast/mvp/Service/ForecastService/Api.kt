package com.example.testappweatherforecast.mvp.Service.ForecastService

import com.example.testappweatherforecast.mvp.entity.ForecastData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("forecast?appid=439d4b804bc8187953eb36d2a8c26a02")
    fun makeGetRequest(
        @Query("lat")lat: String,
        @Query("lon")lon: String
    ): Observable<ForecastData>

}