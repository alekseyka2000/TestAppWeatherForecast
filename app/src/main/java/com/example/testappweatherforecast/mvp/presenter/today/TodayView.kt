package com.example.testappweatherforecast.mvp.presenter.today

import com.example.testappweatherforecast.mvp.entity.ForecastData
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TodayView: MvpView {
    fun sendForecastRequest()
    fun setTodayFragment(forecast: ForecastData)
}