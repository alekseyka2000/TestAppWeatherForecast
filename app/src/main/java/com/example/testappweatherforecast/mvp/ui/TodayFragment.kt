@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.entity.ForecastData
import com.example.testappweatherforecast.mvp.presenter.today.TodayPresenter
import com.example.testappweatherforecast.mvp.presenter.today.TodayView
import kotlinx.android.synthetic.main.fragment_today.*
import moxy.ktx.moxyPresenter

@Suppress("DEPRECATION")
class TodayFragment : BaseFragment(), TodayView{

    private val myPresenter by moxyPresenter { TodayPresenter() }

    override val layoutRes: Int
        get() = R.layout.fragment_today

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendForecastRequest()
    }

    override fun onStart() {
        super.onStart()
        button.setOnClickListener {
            val intent = Intent()
            val textMessage: String = "Today in "+ this.cityCountry.text +
                    " (temperature = " + this.temperatureWeather +
                    ", humidity = " + this.humiditytTextView +
                    ", precipitation = " + this.precipitationTextView +
                    ", pressure = " + this.pressureTextView +
                    ", wind speed = " + this.windSpeedTextView +
                    ", wind direction = " + this.windDirectionTextView + ")"

            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, textMessage)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share today forecast: "))
        }
    }

    override fun sendForecastRequest() {
        myPresenter.getForecast(requireContext())
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun setTodayFragment(forecast: ForecastData) {

        //set weather image
        context?.packageName
        weatherView.setImageDrawable(resources.getDrawable(resources
            .getIdentifier(("pic"+forecast.list[0].weather[0].icon),  "drawable", context?.packageName)) )

        //set city text
        val country = if (forecast.city.country == "none") "" else (", " + forecast.city.country)
        cityCountry.text = (forecast.city.name + country)

        //set temperature text
        temperatureWeather.text = ((forecast.list[0].main.temp - 273).toInt().toString() + " ℃ | "
                + forecast.list[0].weather[0].description.capitalize())

        //set humidity text
        humiditytTextView.text = (forecast.list[0].main.humidity.toString()+" %")

        //set precipitation text
        //used API is'n share precipitation value/ so will be use default value 1 !!!!!
        precipitationTextView.text = "1.0 mm"

        //set pressure text
        // incorrect icon for that value!!!!!!!!!!
        pressureTextView.text = forecast.list[0].main.pressure.toString()+" hPa"

        //set wind speed text
        windSpeedTextView.text = forecast.list[0].wind.speed.toString()+" km/h"

        //set wind direction text
        windDirectionTextView.text = when(forecast.list[0].wind.deg*10){
            in 0 until 225 -> "N"
            in 225 until 675 -> "NE"
            in 675 until 1125 -> "E"
            in 1125 until 1575 -> "SE"
            in 1575 until 2025 -> "S"
            in 2025 until 2475 -> "SW"
            in 2475 until 2925 -> "W"
            in 2925 until 3375 -> "NW"
            in 3375 until 3600 -> "N"
            else -> "Error"
        }

        //make data visible
        progressBar2.visibility = View.INVISIBLE
        containerView.visibility = View.VISIBLE

    }

}
