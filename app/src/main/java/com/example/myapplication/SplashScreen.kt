package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myapplication.databinding.FragmentSignUpBinding
import com.example.myapplication.databinding.FragmentSplashScreenBinding


class SplashScreen : Fragment() {
    private lateinit var navcontroller: NavController
    lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navcontroller = Navigation.findNavController(view)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {

            navcontroller.navigate(R.id.action_splashScreen_to_signIn)

        }, 3000)

    }


}