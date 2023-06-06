package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.databinding.FragmentLoginBinding
import com.bangkit.emergenz.ui.activity.MainActivity
import com.bangkit.emergenz.ui.viewmodel.LoginViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        loginViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.isFinished.observe(viewLifecycleOwner){
            isMovingTime(it)
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        loginViewModel.toast.observe(viewLifecycleOwner){ fungus ->
            showToast(fungus)
        }

        binding.tvRegisterNow.setOnClickListener{
            view.findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }
        binding.btnLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when{
                email.isEmpty() -> Toast.makeText(context, R.string.email_empty, Toast.LENGTH_SHORT).show()
                !isEmail(email) -> Toast.makeText(context, R.string.email_warning, Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(context, R.string.pw_empty, Toast.LENGTH_SHORT).show()
                password.length < 8 -> Toast.makeText(context, R.string.pw_warning, Toast.LENGTH_SHORT).show()
                else -> postLogin(email, password)
            }
        }
    }

    private fun postLogin(email: String, password: String){
        loginViewModel.postLoginIntent(email, password)
    }

    private fun isEmail(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isMovingTime(isFinished: Boolean?) {
        if(isFinished == true){
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        if (!isAdded){

            return
        }
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
            binding.tvRegisterNow.isEnabled = false
            binding.btnLogin.isEnabled = false
        } else {
            binding.progressBarLogin.visibility = View.GONE
            binding.tvRegisterNow.isEnabled = true
            binding.btnLogin.isEnabled = true
        }
    }

    private fun showToast(fungus: String?) {
        Toast.makeText(requireActivity(), fungus, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.progressBarLogin.visibility = View.GONE
        binding.tvRegisterNow.isEnabled = true
        binding.btnLogin.isEnabled = true
        _binding = null
    }
}