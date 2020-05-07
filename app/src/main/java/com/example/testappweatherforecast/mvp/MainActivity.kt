@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp

import android.os.Bundle
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.ui.FlowFragment
import com.example.testappweatherforecast.mvp.ui.MainView
import moxy.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity(), MainView {

    private val flowFragment = FlowFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerForFlowFragment, flowFragment).commitNow()
        }
    }
}