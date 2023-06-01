package com.bangkit.emergenz.ui.fragment

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.emergenz.databinding.FragmentMainBinding
import com.bangkit.emergenz.ui.viewmodel.LocViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private lateinit var locViewModel: LocViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locViewModel = ViewModelProvider(requireActivity())[LocViewModel::class.java]
        testButton()
    }

    private fun testButton(){
        binding.button.setOnClickListener {
            coroutineScope.launch {
                val waitLoc = async {
                    getMyLocation()
                }
                delay(200)
                waitLoc.await()
                if (location!=null){
                    val args = "${location!!.latitude}%2C${location!!.longitude}"
                    locViewModel.setArgument(args)
                    val bottomSheetFragment = CallPageFragment()
                    bottomSheetFragment.show(requireActivity().supportFragmentManager, "CallPageFragment")
                }
            }
        }
    }

    private fun getMyLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                    Log.d(
                        ContentValues.TAG,
                        "getLastLocation: ${location.latitude},${location.longitude}"
                    )
                } else {
                    Toast.makeText(activity, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        ContentValues.TAG,
                        "getLastLocation: ${location?.latitude}, ${location?.longitude}"
                    )
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                fineLocationPermission
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireActivity(),
                coarseLocationPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(fineLocationPermission, coarseLocationPermission),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}