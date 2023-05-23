package com.bangkit.emergenz

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.emergenz.databinding.ActivityMainBinding
import com.bangkit.emergenz.ui.fragment.CallPageFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
        testButton()
    }

    private fun setBottomNavigation(){
        binding.bottomNav.background = null
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> Toast.makeText(this, "To Home", Toast.LENGTH_SHORT).show()
                R.id.action_contact -> Toast.makeText(this, "To Contact", Toast.LENGTH_SHORT).show()
                R.id.action_article -> Toast.makeText(this, "To Article", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun testButton(){
        binding.button.setOnClickListener {
            val bottomSheetFragment = CallPageFragment()
            bottomSheetFragment.show(supportFragmentManager, "CallPageFragment")
        }
    }
}