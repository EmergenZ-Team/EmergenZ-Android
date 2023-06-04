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

        val initialX = -imgSplashScreen.width.toFloat()
        val finalX = (windowManager.defaultDisplay.width - imgSplashScreen.width) / 4f

        // Create the ObjectAnimator
        val animator = ObjectAnimator.ofFloat(imgSplashScreen, "translationX", initialX, finalX)
        animator.duration = LOGO_MOVE
        animator.interpolator = AccelerateDecelerateInterpolator()

        // Start the animation
        animator.start()
    }

    private fun moveToMain(){
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(i)
            finish()
        }, STARTUP_TIME)
    }

    companion object{
        const val LOGO_MOVE : Long = 600
        const val STARTUP_TIME : Long = 1400
    }
}