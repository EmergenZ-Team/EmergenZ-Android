package com.bangkit.emergenz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.emergenz.databinding.ActivityMainBinding
import com.bangkit.emergenz.ui.fragment.CallPageFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val bottomSheetFragment = CallPageFragment()
            bottomSheetFragment.show(supportFragmentManager, "MyBottomSheet")
        }
    }
}