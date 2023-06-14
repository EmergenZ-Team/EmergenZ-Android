package com.bangkit.emergenz.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.FragmentRegisterBinding
import com.bangkit.emergenz.ui.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerViewModel: RegisterViewModel

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
        playAnimation()
        registerViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        registerViewModel.toast.observe(viewLifecycleOwner){bungus ->
            showToast(bungus)
        }

        registerViewModel.isFinished.observe(viewLifecycleOwner){
            isMovingTime(it)
        }

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

                else -> register(username, email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarRegister.visibility = View.VISIBLE
            binding.imageView.visibility = View.INVISIBLE
            binding.btnRegister.isEnabled = false
            binding.tvLoginNow.isEnabled = false
        } else {
            binding.progressBarRegister.visibility = View.GONE
            binding.imageView.visibility = View.VISIBLE
            binding.btnRegister.isEnabled = true
            binding.tvLoginNow.isEnabled = true
        }
    }

    private fun register(username : String, email: String, password : String) {
        registerViewModel.postRegisterIntent(username, email, password)
    }

    private fun isMovingTime(isFinished: Boolean?) {
        if(isFinished == true){
            view?.findNavController()?.navigate(R.id.action_registerFragment2_to_loginFragment2)
        }
    }

    private fun isEmail(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(bungus: String?) {
        Toast.makeText(requireActivity(), bungus, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        val logoImageView = binding.imageView
        // Get the center X position of the screen
        val centerX = resources.displayMetrics.widthPixels / 2

        // Calculate the translation X values
        val startTranslationX = -centerX + logoImageView.width.toFloat() / 2
        val endTranslationX = -logoImageView.width.toFloat()

        // Animation duration in milliseconds
        val animationDuration = 800L

        // Set the initial position
        logoImageView.translationX = startTranslationX

        // Start the animation
        logoImageView.animate()
            .translationX(endTranslationX)
            .setDuration(animationDuration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        val descTextVIew = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val tvUsername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLoginNow, View.ALPHA, 1f).setDuration(500)

        val etEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val etPwd = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val etUsername = ObjectAnimator.ofFloat(binding.edRegisterUsername, View.ALPHA, 1f).setDuration(500)

        val btnLogin = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val togetherBtn = AnimatorSet().apply {
            playTogether(btnLogin,tvPassword,tvUsername,descTextVIew,tvLogin)
        }
        AnimatorSet().apply {
            playSequentially(togetherBtn,etUsername,etEmail,etPwd)
            start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.btnRegister.setOnClickListener(null)
        binding.progressBarRegister.visibility = View.GONE
        binding.tvLoginNow.isEnabled = true
        binding.btnRegister.isEnabled = true
        _binding = null
    }
}