package com.example.testappweatherforecast.mvp.presentation

import LocationService
import android.content.Context
import com.example.testappweatherforecast.mvp.ui.MainView
import moxy.MvpPresenter

class MainPresrnter: MvpPresenter<MainView>(), MainPresenterInterface {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

    fun getLocation(context: Context) {
        LocationService().getLocationRespond(context)
    }

    override fun getLocation(location: String) {

    }
}