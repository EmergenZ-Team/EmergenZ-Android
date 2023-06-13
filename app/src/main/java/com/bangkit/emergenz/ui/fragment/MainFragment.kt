package com.bangkit.emergenz.ui.fragment


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.emergenz.R
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.databinding.FragmentMainBinding
import com.bangkit.emergenz.ui.activity.ProfileActivity
import com.bangkit.emergenz.ui.activity.SettingsActivity
import com.bangkit.emergenz.ui.viewmodel.CallViewModel
import com.bangkit.emergenz.ui.viewmodel.CallViewModelFactory
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
    private lateinit var callViewModel: CallViewModel
    private lateinit var locViewModel: LocViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var clickCount: Int = 0
    private var doubleClickHandler: Handler? = null
    private val doubleClickRunnable = Runnable {
        clickCount = 0
    }
    private var backCount: Int = 0
    private var backPressHandler: Handler? = null
    private val backPressRunnable = Runnable {
        backCount = 0
    }

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
        val apiService = ApiConfig.getApiServiceCall()
        val callRepository = CallRepository(apiService)
        val callViewModelFactory = CallViewModelFactory(callRepository)
        val backPressCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backPressModifier()
            }
        }
        callViewModel = ViewModelProvider(requireActivity(), callViewModelFactory)[CallViewModel::class.java]
        locViewModel = ViewModelProvider(requireActivity())[LocViewModel::class.java]
        setToolbar()
        panicButton()
        topNavMenu()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressCallback)
    }

    private fun backPressModifier(){
        backCount++
        when (backCount) {
            1 -> {
                backPressHandler = Handler(Looper.getMainLooper())
                Toast.makeText(requireActivity(), getString(R.string.exit),Toast.LENGTH_SHORT).show()
                backPressHandler?.postDelayed(backPressRunnable, 1000)
            }
            2 -> {
                requireActivity().finishAffinity()
            }
        }
    }

    private fun setToolbar(){
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.hide()
        activity.supportActionBar?.title = "Home"
    }

    private fun panicButton() {
        binding.btnCircle.setOnClickListener {
            clickCount++

            when {
                clickCount == 1 -> {
                    doubleClickHandler = Handler(Looper.getMainLooper())
                    doubleClickHandler?.postDelayed(doubleClickRunnable, 1000)
                }
                clickCount == 3 -> {
                    coroutineScope.launch {
                        val waitLoc = async {
                            getMyLocation()
                        }
                        delay(200)
                        waitLoc.await()
                        if (location != null) {
                            val args = "${location!!.latitude}%2C${location!!.longitude}"
                            locViewModel.setArgument(args)
                            val bottomSheetFragment = CallPageFragment()
                            bottomSheetFragment.show(
                                requireActivity().supportFragmentManager,
                                "CallPageFragment"
                            )
                        }
                    }
                    clickCount = 0
                    doubleClickHandler?.removeCallbacks(doubleClickRunnable)
                    doubleClickHandler = null
                }
                clickCount > 3 -> {
                    clickCount = 0
                    doubleClickHandler?.removeCallbacks(doubleClickRunnable)
                    doubleClickHandler = null
                }
            }
        }
    }

    private fun getMyLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    this.location = location
                    callViewModel.setDataUrgent()
                } else {
                    Toast.makeText(activity, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
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

    private fun topNavMenu() {
        binding.ivProfile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.ivSetting.setOnClickListener {
            val intent = Intent(requireActivity(), SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        doubleClickHandler?.removeCallbacks(doubleClickRunnable)
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}