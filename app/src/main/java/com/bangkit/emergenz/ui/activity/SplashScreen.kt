package com.bangkit.emergenz.ui.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
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
        moveToMain()
        startup()
    }

    @Suppress("DEPRECATION")
    private fun startup() {
        val imgSplashScreen: ImageView = findViewById(R.id.img_splash_screen)

        imgSplashScreen.alpha = 0f
        imgSplashScreen.animate().setDuration(STARTUP_TIME).alpha(1f).withEndAction {
            val moveToMainActivity = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(moveToMainActivity)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    companion object{
        const val STARTUP_TIME : Long = 1000
    }
}