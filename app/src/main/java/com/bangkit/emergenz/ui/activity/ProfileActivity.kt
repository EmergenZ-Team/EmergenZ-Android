package com.bangkit.emergenz.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}