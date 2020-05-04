package com.example.testappweatherforecast.mvp.presenter.forecast

import com.example.testappweatherforecast.mvp.entity.WeatherList
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ForecastView: MvpView{
    fun sendForecastRequest()
    fun setForecastFragment(weatherList: MutableList<Pair<WeatherList, Int>>)
}