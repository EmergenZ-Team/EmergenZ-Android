package com.bangkit.emergenz.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
//    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLoginNow.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val username = binding.edRegisterUsername.text.toString()
            when {
                username.isEmpty() -> Toast.makeText(
                    context,
                    R.string.username_empty,
                    Toast.LENGTH_SHORT
                ).show()

                email.isEmpty() -> Toast.makeText(context, R.string.email_empty, Toast.LENGTH_SHORT)
                    .show()

                !isEmail(email) -> Toast.makeText(
                    context,
                    R.string.email_warning,
                    Toast.LENGTH_SHORT
                ).show()

                password.isEmpty() -> Toast.makeText(context, R.string.pw_empty, Toast.LENGTH_SHORT)
                    .show()

                password.length < 8 -> Toast.makeText(
                    context,
                    R.string.pw_warning,
                    Toast.LENGTH_SHORT
                ).show()

                else -> view?.findNavController()?.navigate(R.id.action_registerFragment2_to_loginFragment2)
            }
        }
    }

    private fun isEmail(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}