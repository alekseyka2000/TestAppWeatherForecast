package com.example.testappweatherforecast.mvp.presenter.forecast

import LocationService
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.testappweatherforecast.mvp.Service.ForecastService
import com.example.testappweatherforecast.mvp.entity.ForecastData
import com.example.testappweatherforecast.mvp.entity.WeatherList
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@Suppress("DEPRECATION")
@InjectViewState
class ForecastPresenter: MvpPresenter<ForecastView>(){

    fun getForecast(context: Context) {

        val lastLocation = LocationService().getLocationRespond(context)
        fun onShowError(error: Throwable) {
            Toast.makeText(context, error.message , Toast.LENGTH_LONG).show()
        }
        if (isNetworkAvailable(context)){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED)
                lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location!= null) {
                        CompositeDisposable().add(
                            ForecastService.makeGetRequest()
                                .makeGetRequest(location.latitude.toString(), location.longitude.toString())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({response -> onShowForecast(response)}, {t -> onShowError(t) }))
                    }else{
                        val mLocationRequest = LocationRequest()
                        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        mLocationRequest.interval = 0
                        mLocationRequest.fastestInterval = 0
                        mLocationRequest.numUpdates = 1

                        val locationClient = LocationServices
                            .getFusedLocationProviderClient(context)
                        locationClient!!.requestLocationUpdates(
                            mLocationRequest, mLocationCallback,
                            Looper.myLooper()
                        )
                    }
                }
        }else Toast.makeText(context, "Check internet connection" , Toast.LENGTH_LONG).show()
    }

    private fun onShowForecast(forecast: ForecastData) {
        val forecastList = mutableListOf<Pair<WeatherList, Int>>()
        if (forecast.list[0].dt_txt.takeLast(8)!= "00:00:00") forecastList.add(forecast.list[0] to 1)
        for (weather in forecast.list){
            if (weather.dt_txt.takeLast(8)=="00:00:00")
                forecastList.add(weather to 2)
            forecastList.add(weather to 3)
        }
        viewState.setForecastFragment(forecastList)
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewState.sendForecastRequest()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null
    }
}