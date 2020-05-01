@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp

import LocationService
import android.os.Bundle
import android.widget.Toast
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.ui.FlowFragment
import com.example.testappweatherforecast.mvp.ui.MainView
import moxy.MvpAppCompatActivity

 class MainActivity : MvpAppCompatActivity(), MainView {

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         if(savedInstanceState == null){
             supportFragmentManager.beginTransaction()
                 .replace(R.id.containerForFlowFragment, FlowFragment()).commitNow()
         }
     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                             grantResults: IntArray) {
         Toast.makeText(this,"6", Toast.LENGTH_LONG).show()
         LocationService().handlePermissionsResult(requestCode, grantResults, this)
     }
 }