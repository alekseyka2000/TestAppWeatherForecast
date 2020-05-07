@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui.today

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.entity.TodayDB
import com.example.testappweatherforecast.mvp.presenter.today.TodayPresenter
import com.example.testappweatherforecast.mvp.presenter.today.TodayView
import com.example.testappweatherforecast.mvp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_today.*
import moxy.ktx.moxyPresenter

@Suppress("DEPRECATION")
class TodayFragment : BaseFragment(), TodayView, TodayConnectivityReceiver.TodayConnectivityReceiverListener {

    private val myPresenter by moxyPresenter { TodayPresenter() }

    override val layoutRes: Int
        get() = R.layout.fragment_today

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendForecastRequest()
        requireContext().registerReceiver(TodayConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStart() {
        super.onStart()
        button.setOnClickListener {
            val intent = Intent()
            val textMessage: String = "Today in "+ this.cityCountry.text +
                    " (temperature = " + this.temperatureWeather.text +
                    ", humidity = " + this.humiditytTextView.text +
                    ", precipitation = " + this.precipitationTextView.text +
                    ", pressure = " + this.pressureTextView.text +
                    ", wind speed = " + this.windSpeedTextView.text +
                    ", wind direction = " + this.windDirectionTextView.text + ")"

            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, textMessage)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share today forecast: "))
        }
    }

    override fun onResume() {
        super.onResume()
        TodayConnectivityReceiver.todayConnectivityReceiverListener = this
    }

    override fun sendForecastRequest() {
        myPresenter.getForecast(requireContext())
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun setTodayFragment(forecast: List<TodayDB>) {

        //set weather image
        context?.packageName
        weatherView.setImageDrawable(resources.getDrawable(resources
            .getIdentifier(("pic"+forecast[0].icon),  "drawable", context?.packageName)) )

        //set city text
        val country = if (forecast[0].country == "none") "" else (", " + forecast[0].country)
        cityCountry.text = (forecast[0].city + country)

        //set temperature text
        temperatureWeather.text = ((forecast[0].temp - 273).toInt().toString() + " ℃ | "
                + forecast[0].description.capitalize())

        //set humidity text
        humiditytTextView.text = (forecast[0].humidity.toString()+" %")

        //set precipitation text
        //used API is'n share precipitation value/ so will be use default value 1 !!!!!
        precipitationTextView.text = "1.0 mm"

        //set pressure text
        // incorrect icon for that value!!!!!!!!!!
        pressureTextView.text = forecast[0].pressure.toString()+" hPa"

        //set wind speed text
        windSpeedTextView.text = forecast[0].speed.toString()+" km/h"

        //set wind direction text
        windDirectionTextView.text = when(forecast[0].deg*10){
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (isConnected) {
            sendForecastRequest()
        }
    }

    override fun requestPermissions(){
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION),200)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        if (requestCode == 200) {
                myPresenter.getForecast(requireContext())
        }
    }
}
