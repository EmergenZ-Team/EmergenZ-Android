package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.databinding.FragmentProfileBinding
import com.bangkit.emergenz.ui.activity.AuthActivity
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

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        profileViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[ProfileViewModel::class.java]

        binding.btnIdRegister.setOnClickListener{
            view.findNavController().navigate(R.id.action_profileFragment_to_registerKtpFragment)
        }

        binding.btnLogout.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        profileViewModel.saveToken("")
        profileViewModel.setSession(false)
        startActivity(intent)
        requireActivity().finish()
    }
}