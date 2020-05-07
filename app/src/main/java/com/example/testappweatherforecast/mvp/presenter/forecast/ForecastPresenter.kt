package com.example.testappweatherforecast.mvp.presenter.forecast

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.testappweatherforecast.mvp.Service.ForecastDBd.ForecastDao
import com.example.testappweatherforecast.mvp.Service.ForecastDBd.ForecastRoomDB
import com.example.testappweatherforecast.mvp.Service.ForecastService.ForecastService
import com.example.testappweatherforecast.mvp.entity.ForecastDB
import com.example.testappweatherforecast.mvp.entity.ForecastData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter

@Suppress("DEPRECATION")
@InjectViewState
class ForecastPresenter: MvpPresenter<ForecastView>(){

    private var dbForecast: ForecastRoomDB? = null
    private var forecastDao: ForecastDao? = null

    fun getForecast(context: Context) {

        dbForecast = ForecastRoomDB.getDatabase(context)
        forecastDao = dbForecast?.forecastDao()

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val lastLocation = mFusedLocationClient!!.lastLocation

        fun onShowError(error: Throwable) {
            Toast.makeText(context, error.message , Toast.LENGTH_LONG).show()
        }
        if(checkPermissions(context)){
            if (isLocationEnable(context)){
                if (isNetworkAvailable(context)){
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                        == PackageManager.PERMISSION_GRANTED)
                        lastLocation.addOnCompleteListener { task ->
                            val location: Location? = task.result
                            if (location!= null) {
                                CompositeDisposable().add(
                                    ForecastService.makeGetRequest()
                                        .makeGetRequest(location.latitude.toString(), location.longitude.toString())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe({response -> onSetForecastDB(response)}, {t -> onShowError(t) }))
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
                }else{
                    var list: List<ForecastDB>
                    GlobalScope.launch {
                        list = dbForecast?.forecastDao()?.getAll()!!
                        withContext(Dispatchers.Main) {
                            if(list.isEmpty()){
                                Toast.makeText(context, "Forecast data is not your device, turn on the Internet for start work" , Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(context, "Check internet connection" , Toast.LENGTH_LONG).show()
                                onShowForecast(forecast = list)
                            }
                        }
                    }

                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent) }
        } else {
            viewState.requestPermissions()
        }
    }

    private fun onSetForecastDB(forecast: ForecastData){
        var list: List<ForecastDB>
        GlobalScope.launch {
            dbForecast?.forecastDao()?.deleteAll()
            val insertDB = ForecastDB()
            insertDB.city = forecast.city.name
            insertDB.country = forecast.city.country
            for(listItem in forecast.list){
                insertDB.temp = listItem.main.temp
                insertDB.humidity = listItem.main.humidity
                insertDB.pressure = listItem.main.pressure
                insertDB.speed = listItem.wind.speed
                insertDB.deg = listItem.wind.deg
                insertDB.dtTxt = listItem.dt_txt
                insertDB.description = listItem.weather[0].description
                insertDB.icon = listItem.weather[0].icon
                insertDB.main = listItem.weather[0].main
                dbForecast?.forecastDao()?.insert(insertDB)
            }

            list = dbForecast?.forecastDao()?.getAll()!!

            withContext(Dispatchers.Main) {
                onShowForecast(forecast = list)
            }
        }
    }

    private fun onShowForecast(forecast: List<ForecastDB>) {
        val forecastList = mutableListOf<Pair<ForecastDB, Int>>()
        if (forecast[0].dtTxt.takeLast(8)!= "00:00:00") forecastList.add(forecast[0] to 1)
        var check = ""
        for (weather in forecast){
                if(check != weather.dtTxt){
                    check = weather.dtTxt
                    if (weather.dtTxt.takeLast(8)=="00:00:00")
                        forecastList.add(weather to 2)
                    forecastList.add(weather to 3)
                }
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

    //check if location has turned on from the setting
    private fun isLocationEnable(context: Context) :Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(context: Context): Boolean{
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

}