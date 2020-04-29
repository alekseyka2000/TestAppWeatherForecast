package com.example.testappweatherforecast.mvp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testappweatherforecast.R
import kotlinx.android.synthetic.main.fragment_flow.*

class FlowFragment : BaseFragment() {

    var currentFragment: String = "one"

    override val layoutRes: Int
        get() = R.layout.fragment_flow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //create fragments
        childFragmentManager.inTransaction {
            add(R.id.container, TodayFragment(),"one")
        }

        fun createOtherFragment(fragment: Fragment, TAG: String ){
            childFragmentManager.inTransaction {
                add(R.id.container, fragment,TAG)
                hide(fragment)
            }
        }


        createOtherFragment(ForecastFragment(),"two")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        nav_view.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.navigation_movies_list ->{
                    //getData()
                    showFragment("one")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite_actors ->{showFragment("two")
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    fun showFragment(s:String){
        childFragmentManager.inTransaction {
            hide(childFragmentManager.findFragmentByTag(currentFragment)!!)
            show(childFragmentManager.findFragmentByTag(s)!!)
        }
        currentFragment=s
    }
}

//
private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
