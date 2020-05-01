@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui

import android.os.Bundle
import android.view.View
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.entity.ForecastData
import com.example.testappweatherforecast.mvp.presenter.forecast.ForecastPresenter
import com.example.testappweatherforecast.mvp.presenter.forecast.ForecastView
import kotlinx.android.synthetic.main.fragment_forecast.*
import moxy.ktx.moxyPresenter

@Suppress("DEPRECATION")
class ForecastFragment : BaseFragment(),
    ForecastView {

    private val myPresenter by moxyPresenter { ForecastPresenter() }

    override val layoutRes: Int
        get() = R.layout.fragment_forecast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendForecastRequest()
    }

    override fun sendForecastRequest() {
        myPresenter.getForecast(requireContext())
    }

    override fun setForecastFragment(forecast: ForecastData) {
        city.setText(forecast.city.toString())
    }

}



