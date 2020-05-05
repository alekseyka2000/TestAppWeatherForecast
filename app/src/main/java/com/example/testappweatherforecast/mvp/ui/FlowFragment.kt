@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testappweatherforecast.R
import com.example.testappweatherforecast.mvp.ui.forecast.ForecastFragment
import kotlinx.android.synthetic.main.fragment_flow.*
import moxy.MvpView

@Suppress("DEPRECATION")
class FlowFragment : BaseFragment(), MvpView {

    private var currentFragment: String? = null

    override val layoutRes: Int
        get() = R.layout.fragment_flow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentFragment = savedInstanceState?.getString("CurrentFragment") ?: "one"
        if (savedInstanceState == null) {
            val todayFragment = TodayFragment()
            val forecastFragment =
                ForecastFragment()
            childFragmentManager.inTransaction {
                add(R.id.container, todayFragment, "one")
                hide(todayFragment)
                show(todayFragment)
                add(R.id.container, forecastFragment, "two")
                hide(forecastFragment)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.navigation_movies_list ->{
                    showFragment("one")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite_actors ->{
                    showFragment("two")
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun showFragment(s:String){
        childFragmentManager.inTransaction {
            hide(childFragmentManager.findFragmentByTag(currentFragment)!!)
            show(childFragmentManager.findFragmentByTag(s)!!)
        }
        currentFragment=s
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("CurrentFragment", currentFragment)
    }
}

//
private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
