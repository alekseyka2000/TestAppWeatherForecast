package com.example.testappweatherforecast.mvp.presenter.forecast

import android.content.Context
import com.example.testappweatherforecast.mvp.entity.ForecastData
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ForecastView: MvpView{
    fun sendForecastRequest()
    fun setForecastFragment(forecast: ForecastData)
}