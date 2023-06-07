package com.bangkit.emergenz.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import android.widget.ImageView
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.ActivitySplashScreenBinding
import com.bangkit.emergenz.ui.viewmodel.TokenViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
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
        val pref = UserPreferences.getInstance(dataStore)
        val tokenViewModel = ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]
        tokenViewModel.getToken().observe(this){
            token = it
        }
        tokenViewModel.getSession().observe(this) {
            onSession = it
        }
          
        val imgSplashScreen: ImageView = findViewById(R.id.img_splash_screen)

        imgSplashScreen.alpha = 0f
        imgSplashScreen.animate().setDuration(STARTUP_TIME).alpha(1f).withEndAction {
            if(onSession) {
                val moveToMainActivity = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(moveToMainActivity)
            }
            else{
                val moveToAuthActivity = Intent(this@SplashScreen, AuthActivity::class.java)
                startActivity(moveToAuthActivity)

            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    companion object{
        const val STARTUP_TIME : Long = 1000
    }
}