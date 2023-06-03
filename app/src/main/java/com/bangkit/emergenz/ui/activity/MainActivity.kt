package com.bangkit.emergenz.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.ActivityMainBinding
import com.bangkit.emergenz.ui.fragment.ContactFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
    }

    private fun setBottomNavigation(){
        binding.bottomNav.background = null
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> findNavController(R.id.nav_host_fragment_content_main).popBackStack(
                    R.id.mainFragment, false)
                R.id.action_contact ->
                    if (!allPermissionsGranted()) {
                        ActivityCompat.requestPermissions(
                            this,
                            REQUIRED_PERMISSIONS,
                            REQUEST_CODE_PERMISSIONS
                        )
                    } else {
                        findNavController(R.id.nav_host_fragment_content_main).navigate(
                            R.id.contactFragment)
                    }
                R.id.action_article -> findNavController(R.id.nav_host_fragment_content_main).navigate(
                    R.id.articleFragment
                )
            }
            true
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
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}