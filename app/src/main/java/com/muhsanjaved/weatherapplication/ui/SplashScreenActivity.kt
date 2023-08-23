package com.muhsanjaved.weatherapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muhsanjaved.weatherapplication.R
import com.muhsanjaved.weatherapplication.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}