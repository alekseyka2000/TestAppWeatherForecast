@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui.forecast

import android.os.Bundle
import android.view.View
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
class ForecastFragment : BaseFragment(), ForecastView {

    private val myPresenter by moxyPresenter { ForecastPresenter() }
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var viewManager: RecyclerView.LayoutManager? = null

    override val layoutRes: Int
        get() = R.layout.fragment_forecast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendForecastRequest()
    }

    override fun sendForecastRequest() {
        myPresenter.getForecast(requireContext())
        progressBar.visibility = View.INVISIBLE
        recycleView.visibility = View.VISIBLE
    }

    override fun setForecastFragment(weatherList: MutableList<Pair<ForecastDB, Int>>) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = Adapter(
            weatherList,
            requireContext()
        )
        recycleView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }



}




