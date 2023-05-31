package com.bangkit.emergenz.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = "Profile"
        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnIdRegister.setOnClickListener{
            view.findNavController().navigate(R.id.action_profileFragment_to_registerKtpFragment)
        }
    }
}