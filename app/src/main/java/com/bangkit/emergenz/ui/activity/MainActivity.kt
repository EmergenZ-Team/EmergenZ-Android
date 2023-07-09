package com.bangkit.emergenz.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.databinding.ActivityMainBinding
import com.bangkit.emergenz.ui.viewmodel.ProfileViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory

private val Context.dataStore2: DataStore<Preferences> by preferencesDataStore(name = "token")

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore2)
        profileViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ProfileViewModel::class.java]

        profileViewModel.getEmail().observe(this){email ->
            profileViewModel.getDetailIntent(email)
            checkValid()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        setBottomNavigation()
    }

    private fun checkValid() {
        profileViewModel.chckErr.observe(this){query->
            if (query){
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun setBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    navController.popBackStack(R.id.mainFragment, false)
                    true
                }

                R.id.action_contact -> {
                    if (!allPermissionsGranted()) {
                        ActivityCompat.requestPermissions(
                            this,
                            REQUIRED_PERMISSIONS,
                            REQUEST_CODE_PERMISSIONS
                        )
                        navController.popBackStack(R.id.mainFragment, false)
                    } else {
                        navController.navigate(R.id.contactFragment)
                    }
                    true
                }

                R.id.action_article -> {
                    navController.navigate(R.id.articleFragment)
                    true
                }

                R.id.action_history -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }

                else -> false
            }
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentDestination = navController.currentDestination?.id
        val homeFragmentId = R.id.mainFragment

        if (currentDestination == homeFragmentId) {
            super.onBackPressed()
        } else {
            navController.popBackStack(homeFragmentId, false)
            binding.bottomNav.selectedItemId = R.id.action_home
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.perm_lack),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}