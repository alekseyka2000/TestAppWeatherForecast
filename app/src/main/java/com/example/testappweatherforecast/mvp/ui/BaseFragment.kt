@file:Suppress("DEPRECATION")

package com.example.testappweatherforecast.mvp.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
abstract class BaseFragment : MvpAppCompatFragment() {

    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )= inflater.inflate(layoutRes, container, false)
}
