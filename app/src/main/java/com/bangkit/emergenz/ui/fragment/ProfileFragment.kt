package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.response.GetDetailResponse
import com.bangkit.emergenz.databinding.FragmentProfileBinding
import com.bangkit.emergenz.ui.activity.AuthActivity
import com.bangkit.emergenz.ui.activity.MainActivity
import com.bangkit.emergenz.ui.viewmodel.ProfileViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel : ProfileViewModel

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
        val backPressCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        profileViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[ProfileViewModel::class.java]

        profileViewModel.getEmail().observe(viewLifecycleOwner){email ->
            setEmail(email)
            profileViewModel.getDetailIntent(email)
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        profileViewModel.detailUser.observe(viewLifecycleOwner){detailUser ->
            showDetailUser(detailUser)
        }

        binding.btnIdRegister.setOnClickListener{
            view.findNavController().navigate(R.id.action_profileFragment_to_registerKtpFragment)
        }

        binding.btnLogout.setOnClickListener{
            logout()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
    }

    fun onBackPressed() {
        if (profileViewModel.getError() == true) {
            Toast.makeText(requireActivity(), "Mohon isi detail terlebih dahulu",Toast.LENGTH_SHORT).show()

        }else{
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showDetailUser(detailUser: GetDetailResponse?) {
        if (detailUser != null){
            binding.apply {
                tvFullName.text = detailUser.data?.name
                tvNipFilled.text = detailUser.data?.nik
                tvGenderFilled.text = detailUser.data?.gender
                tvProvinceFilled.text = detailUser.data?.province
                tvCityFilled.text = detailUser.data?.city
                tvAddressFilled.text = detailUser.data?.address
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.apply {
                tvNip.visibility = View.GONE
                tvNipFilled.visibility = View.GONE
                tvGender.visibility = View.GONE
                tvGenderFilled.visibility = View.GONE
                tvProvince.visibility = View.GONE
                tvProvinceFilled.visibility = View.GONE
                tvAddress.visibility = View.GONE
                tvAddressFilled.visibility = View.GONE
                tvCity.visibility = View.GONE
                tvCityFilled.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
        else {
            binding.apply {
                tvNip.visibility = View.VISIBLE
                tvNipFilled.visibility = View.VISIBLE
                tvGender.visibility = View.VISIBLE
                tvGenderFilled.visibility = View.VISIBLE
                tvProvince.visibility = View.VISIBLE
                tvProvinceFilled.visibility = View.VISIBLE
                tvAddress.visibility = View.VISIBLE
                tvAddressFilled.visibility = View.VISIBLE
                tvCity.visibility = View.VISIBLE
                tvCityFilled.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun setEmail(email: String?) {
        binding.tvEmail.text = email
    }

    private fun logout() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        profileViewModel.saveToken("")
        profileViewModel.setSession(false)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}