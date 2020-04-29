package com.example.testappweatherforecast.mvp.ui

import android.widget.TextView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView{
    fun getLocation(location: String)
}