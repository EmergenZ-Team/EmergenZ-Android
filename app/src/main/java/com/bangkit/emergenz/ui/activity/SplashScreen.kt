package com.bangkit.emergenz.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    private var onSession: Boolean = false
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startup()
    }

    private fun startup() {
        Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(i)
            finish()
        }, STARTUP_TIME)
    }
    companion object{
        const val STARTUP_TIME : Long = 2000
    }
}