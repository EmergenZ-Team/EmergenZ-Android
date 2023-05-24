package com.bangkit.emergenz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bangkit.emergenz.databinding.ActivityMainBinding

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
                R.id.action_home -> findNavController(R.id.nav_host_fragment_content_main).popBackStack(R.id.mainFragment, false)
                R.id.action_contact -> findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.contactFragment)
                R.id.action_article -> findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.articleFragment)
            }
            true
        }
    }

}