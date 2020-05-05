package com.example.testappweatherforecast.mvp.presenter.today

import LocationService
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.testappweatherforecast.mvp.Service.ForecastDao
import com.example.testappweatherforecast.mvp.Service.ForecastRoomDB
import com.example.testappweatherforecast.mvp.Service.ForecastService
import com.example.testappweatherforecast.mvp.entity.ForecastDB
import com.example.testappweatherforecast.mvp.entity.ForecastData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter

@Suppress("DEPRECATION")
@InjectViewState
class TodayPresenter: MvpPresenter<TodayView>(){

    private var db: ForecastRoomDB? = null
    private var forecastDao: ForecastDao? = null

    fun getForecast(context: Context) {

        db = ForecastRoomDB.getDatabase(context)
        forecastDao = db?.forecastDao()

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
                                .subscribe({response -> onSetDB(response)}, {t -> onShowError(t) }))
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
            Toast.makeText(context, "Check internet connection" , Toast.LENGTH_LONG).show()
            var list = listOf<ForecastDB>()
            GlobalScope.launch {
                list = forecastDao?.getAll()!!
                if(list!=null) onShowForecast(forecast = list)
            }
        }
    }

    private fun onSetDB(forecast: ForecastData){
        var list = listOf<ForecastDB>()
        GlobalScope.launch {
            db?.forecastDao()?.deleteAll()
            var insertDB = ForecastDB()
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
                db?.forecastDao()?.insert(insertDB)
            }

            list = forecastDao?.getAll()!!
            if(list!=null) onShowForecast(forecast = list)
        }
    }

    private fun onShowForecast(forecast: List<ForecastDB>) {
        viewState.setTodayFragment(forecast)
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