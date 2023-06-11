package com.bangkit.emergenz.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.adapter.CombinedAdapter
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.databinding.FragmentRvCallBinding
import com.bangkit.emergenz.ui.viewmodel.CallViewModel
import com.bangkit.emergenz.ui.viewmodel.LocViewModel
import com.bangkit.emergenz.ui.viewmodel.CallViewModelFactory
import com.bangkit.emergenz.util.animateVisibility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RvCallFragment(var query: String) : Fragment() {
    private var _binding: FragmentRvCallBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var callViewModel: CallViewModel
    private lateinit var locViewModel: LocViewModel
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
        val apiService = ApiConfig.getApiServiceCall()
        val callRepository = CallRepository(apiService)
        val callViewModelFactory = CallViewModelFactory(callRepository)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        callViewModel = ViewModelProvider(requireActivity(), callViewModelFactory)[CallViewModel::class.java]
        locViewModel = ViewModelProvider(requireActivity())[LocViewModel::class.java]

        coroutineScope.launch {
            val waitSearch = async {
                setSearchPlace()
            }
            waitSearch.await()
            delay(500)
            getPlaceDetail()
            delay(500)
            setCombinedRecycleView()
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

    private fun setCombinedRecycleView(){
        val recyclerView = binding.rvCall
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = CombinedAdapter(emptyList(), emptyList(), query, requireContext())
        recyclerView.layoutManager = layoutManager

        when (query) {
            FIRE -> {
                callViewModel.dataUrgentFire.observe(viewLifecycleOwner){urgentFire ->
                    adapter.setData1(urgentFire)
                }
                callViewModel.dataFire.observe(viewLifecycleOwner){fire ->
                    adapter.setData2(fire)
                }
            }
            HOSPITAL -> {
                callViewModel.dataUrgentHospital.observe(viewLifecycleOwner){urgentHospital ->
                    adapter.setData1(urgentHospital)
                }
                callViewModel.dataHospital.observe(viewLifecycleOwner){hospital ->
                    adapter.setData2(hospital)
                }
            }
            POLICE -> {
                callViewModel.dataUrgentPolice.observe(viewLifecycleOwner){urgentPolice ->
                    adapter.setData1(urgentPolice)
                }
                callViewModel.dataPolice.observe(viewLifecycleOwner){police ->
                    adapter.setData2(police)
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
