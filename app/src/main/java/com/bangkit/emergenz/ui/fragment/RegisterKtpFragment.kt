package com.bangkit.emergenz.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.databinding.FragmentRegisterKtpBinding
import com.bangkit.emergenz.ui.activity.CameraActivity
import com.bangkit.emergenz.ui.viewmodel.UploadDetailViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory
import com.bangkit.emergenz.util.rotateFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class RegisterKtpFragment : Fragment() {
    private var _binding: FragmentRegisterKtpBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private var email : String? = null
    private lateinit var uploadDetailViewModel: UploadDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterKtpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backPressCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        showLoading(false)

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        uploadDetailViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[UploadDetailViewModel::class.java]

        uploadDetailViewModel.getEmail().observe(viewLifecycleOwner){email ->
            setEmail(email)
        }

        uploadDetailViewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            showLoading(isLoading)
        }

        uploadDetailViewModel.toast.observe(viewLifecycleOwner){bungus ->
            showToast(bungus)
        }

        uploadDetailViewModel.isFinished.observe(viewLifecycleOwner){
            isFinishTime(it)
        }

        binding.apply {
            edEmail.setText(email)
            btnCamera.setOnClickListener { startCameraX() }
            btnConfirm.setOnClickListener { uploadImage() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
    }

    private fun setEmail(email: String?) {
        binding.edEmail.setText(email ?: "No Email")
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.perm_lack),
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage() {
        var checkValid = true

        when {
            binding.edFullName.text.toString().isBlank()-> {
                Toast.makeText(
                    context,
                    R.string.full_name_empty,
                    Toast.LENGTH_SHORT
                ).show()
                checkValid = false
            }
            binding.edNik.text.toString().isBlank()-> {
                Toast.makeText(
                    context,
                    R.string.nik_empty,
                    Toast.LENGTH_SHORT
                ).show()
                checkValid = false
            }

            binding.edProvince.text.toString().isBlank() -> {
                Toast.makeText(
                    context,
                    R.string.province_empty,
                    Toast.LENGTH_SHORT
                ).show()
                checkValid = false
            }

            binding.edCity.text.toString().isBlank() -> {
                Toast.makeText(
                    context,
                    R.string.city_empty,
                    Toast.LENGTH_SHORT
                ).show()
                checkValid = false
            }

            binding.edAddress.text.toString().isBlank() -> {
                Toast.makeText(
                    context,
                    R.string.address_empty,
                    Toast.LENGTH_SHORT
                ).show()
                checkValid = false
            }

            getFile == null -> {
                Toast.makeText(requireActivity(), getString(R.string.img_lack), Toast.LENGTH_SHORT).show()
                checkValid = false
            }
        }

        if (checkValid){
            val file = uploadDetailViewModel.reduceFileSize(getFile as File)
            val email = binding.edEmail.text.toString().toRequestBody("text/plain".toMediaType())
            val fullName = binding.edFullName.text.toString().toRequestBody("text/plain".toMediaType())
            val nik = binding.edNik.text.toString().toRequestBody("text/plain".toMediaType())
            var gender : RequestBody = "U".toRequestBody("text/plain".toMediaType())
            when {
                binding.rbMale.isChecked -> {
                    gender = "M".toRequestBody("text/plain".toMediaType())
                }
                binding.rbFemale.isChecked -> {
                    gender = "F".toRequestBody("text/plain".toMediaType())
                }
            }
            val province = binding.edProvince.text.toString().toRequestBody("text/plain".toMediaType())
            val city = binding.edCity.text.toString().toRequestBody("text/plain".toMediaType())
            val address = binding.edAddress.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile,
            )
            uploadDetailViewModel.uploadStoryIntent(email, fullName, nik, gender, province, city, address, imageMultipart)
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    @Suppress("DEPRECATION")
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val myFile = it.data?.getSerializableExtra("picture") as? File
        val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true)

        myFile?.let { file ->
            rotateFile(file, isBackCamera)
            getFile = myFile
            binding.ivIdentityCard.setImageBitmap(BitmapFactory.decodeFile(file.path))
        }
    }

    private fun showToast(bungus: String) {
        Toast.makeText(requireActivity(), bungus, Toast.LENGTH_SHORT).show()
    }
    private fun isFinishTime(isFinished: Boolean){
        if(isFinished) {
            view?.findNavController()?.navigate(R.id.action_registerKtpFragment_to_profileFragment)
        }
    }

    private fun showLoading(isLoading : Boolean){
        if(isLoading){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnConfirm.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnConfirm.isEnabled = true
        }
    }

    fun onBackPressed() {
        findNavController().popBackStack(R.id.profileFragment, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}