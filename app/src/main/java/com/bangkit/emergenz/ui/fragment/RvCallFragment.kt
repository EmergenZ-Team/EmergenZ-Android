package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.adapter.CombinedAdapter
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.data.repository.HistoryRepository
import com.bangkit.emergenz.databinding.FragmentRvCallBinding
import com.bangkit.emergenz.ui.viewmodel.CallViewModel
import com.bangkit.emergenz.ui.viewmodel.LocViewModel
import com.bangkit.emergenz.ui.viewmodel.CallViewModelFactory
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModel
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModelFactory
import com.bangkit.emergenz.util.NetworkConnectivityChecker
import com.bangkit.emergenz.util.animateVisibility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class RvCallFragment(var query: String) : Fragment() {
    private var _binding: FragmentRvCallBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var callViewModel: CallViewModel
    private lateinit var locViewModel: LocViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        val callApiService = ApiConfig.getApiServiceCall()
        val callRepository = CallRepository(callApiService)
        val callViewModelFactory = CallViewModelFactory(callRepository)

        val historyApiService = ApiConfigCloud.getApiService()
        val historyRepository = HistoryRepository(historyApiService)
        val pref = UserPreferences.getInstance(requireContext().dataStore)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        callViewModel = ViewModelProvider(requireActivity(), callViewModelFactory)[CallViewModel::class.java]
        locViewModel = ViewModelProvider(requireActivity())[LocViewModel::class.java]
        historyViewModel = ViewModelProvider(requireActivity(), HistoryViewModelFactory(historyRepository, pref))[HistoryViewModel::class.java]

        coroutineScope.launch {
            val waitSearch = async {
                setSearchPlace()
            }
            waitSearch.await()
            delay(500)
            getPlaceDetail()
            delay(500)
            checkConection()
            showLoading(false)
        }
    }

    private fun setSearchPlace() {
        val myLoc= locViewModel.getArgumentLiveData().value.toString()
        val myLocBias= "circle%3A500%40$myLoc"

        callViewModel.getPlaceId(query, myLocBias)
        callViewModel.getSearchText(myLoc, query)
    }

    private fun getPlaceDetail() {
        when (query) {
            FIRE -> {
                callViewModel.getListPlaceIdsF().observe(viewLifecycleOwner) { placeIds ->
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(FIRE, placeId)
                    }
                }
                Log.d("Posisi", "Pemadam")
            }
            POLICE -> {
                callViewModel.getListPlaceIdsP().observe(viewLifecycleOwner) { placeIds ->
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(POLICE, placeId)
                    }
                }
                Log.d("Posisi", "Polisi")
            }
            HOSPITAL -> {
                callViewModel.getListPlaceIdsH().observe(viewLifecycleOwner) { placeIds ->
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(HOSPITAL, placeId)
                    }
                }
                Log.d("Posisi", "Hospital")
            }
        }
    }

    private fun setCombinedRecycleView(internet: Boolean){
        historyViewModel.getName().observe(viewLifecycleOwner){name->
            val recyclerView = binding.rvCall
            val layoutManager = LinearLayoutManager(requireContext())
            val adapter = CombinedAdapter(emptyList(), emptyList(), query, requireContext(), historyViewModel, name)
            recyclerView.layoutManager = layoutManager

            when (query) {
                FIRE -> {
                    callViewModel.dataUrgentFire.observe(viewLifecycleOwner){urgentFire ->
                        adapter.setData1(urgentFire)
                    }
                    if (internet){
                        callViewModel.dataFire.observe(viewLifecycleOwner){fire ->
                            adapter.setData2(fire)
                        }
                    }
                }
                HOSPITAL -> {
                    callViewModel.dataUrgentHospital.observe(viewLifecycleOwner){urgentHospital ->
                        adapter.setData1(urgentHospital)
                    }
                    if (internet){
                        callViewModel.dataHospital.observe(viewLifecycleOwner){hospital ->
                            adapter.setData2(hospital)
                        }
                    }
                }
                POLICE -> {
                    callViewModel.dataUrgentPolice.observe(viewLifecycleOwner){urgentPolice ->
                        adapter.setData1(urgentPolice)
                    }
                    if (internet){
                        callViewModel.dataPolice.observe(viewLifecycleOwner){police ->
                            adapter.setData2(police)
                        }
                    }
                }
            }

            val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter

            recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                val height = recyclerView.computeVerticalScrollRange()
                val layoutParams = recyclerView.layoutParams
                layoutParams.height = height
                recyclerView.layoutParams = layoutParams
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            rvCall.isEnabled = !isLoading

            // Animate views alpha
            if (isLoading) {
                loadingBar3.animateVisibility(true)
            } else {
                loadingBar3.animateVisibility(false)
            }
        }
    }

    private fun checkConection(){
        val networkUtils = NetworkConnectivityChecker(requireContext())
        val isInternetAvailable = networkUtils.isInternetAvailable()

        if (isInternetAvailable) {
            setCombinedRecycleView(true)
        } else {
            setCombinedRecycleView(false)
            Toast.makeText(requireContext(), "Sambungkan ke internet untuk hasil yang lebih lengkap", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val POLICE = "Polisi"
        const val FIRE = "Pemadam Kebakaran"
        const val HOSPITAL = "Rumah Sakit"
    }
}
