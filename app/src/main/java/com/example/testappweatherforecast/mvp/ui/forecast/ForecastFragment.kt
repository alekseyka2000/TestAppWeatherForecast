@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui.forecast

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.entity.ForecastDB
import com.example.testappweatherforecast.mvp.presenter.forecast.ForecastPresenter
import com.example.testappweatherforecast.mvp.presenter.forecast.ForecastView
import com.example.testappweatherforecast.mvp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_forecast.*
import moxy.ktx.moxyPresenter

@Suppress("DEPRECATION")
class ForecastFragment : BaseFragment(), ForecastView, ForecastConnectivityReceiver.ForecastConnectivityReceiverListener {

    private val myPresenter by moxyPresenter { ForecastPresenter() }
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var viewManager: RecyclerView.LayoutManager? = null

    override val layoutRes: Int
        get() = R.layout.fragment_forecast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendForecastRequest()
        getActivity()?.registerReceiver(ForecastConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResume() {
        super.onResume()
        ForecastConnectivityReceiver.forecastConnectivityReceiverListener = this
    }

    override fun sendForecastRequest() {
        myPresenter.getForecast(requireContext())
    }

    override fun setForecastFragment(weatherList: MutableList<Pair<ForecastDB, Int>>) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = Adapter(weatherList, requireContext())
        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        progressBar.visibility = View.INVISIBLE
        recycleView.visibility = View.VISIBLE
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (isConnected) sendForecastRequest()
    }

    override fun requestPermissions(){
        requestPermissions(
            arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), 200)
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




