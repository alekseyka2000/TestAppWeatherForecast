package com.example.testappweatherforecast.mvp.Service

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object ForecastService {
    val api = Retrofit.Builder()
            .baseUrl("https://samples.openweathermap.org/data/2.5/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

    fun makeGetRequest(): Api{
       return api
    }

}
