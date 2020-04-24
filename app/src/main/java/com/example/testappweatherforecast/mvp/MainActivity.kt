 package com.example.testappweatherforecast.mvp

import LocationService
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.presentation.MainPresrnter
import com.example.testappweatherforecast.mvp.ui.MainView
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

 class MainActivity : MvpAppCompatActivity(), MainView {
     val myPresenter by moxyPresenter { MainPresrnter() }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         button.setOnClickListener{
             myPresenter.getLocation(this)
         }
     }

     override fun getLocation(location: String) {
         text.setText(location)
     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
         grantResults: IntArray) {
         LocationService().handlePermissionsResult(requestCode, permissions, grantResults)
     }
 }