package com.bangkit.emergenz.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.emergenz.R
import com.bangkit.emergenz.databinding.FragmentRegisterKtpBinding
import com.bangkit.emergenz.util.rotateFile
import java.io.File

class RegisterKtpFragment : Fragment() {
    private var _binding: FragmentRegisterKtpBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null

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
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnConfirm.setOnClickListener { uploadImage() }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadImage() {

    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraFragment::class.java)
        launcherIntentCameraX.launch(intent)
    }

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

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}