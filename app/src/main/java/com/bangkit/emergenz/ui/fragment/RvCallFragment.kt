package com.bangkit.emergenz.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.adapter.CallAdapter
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.databinding.FragmentRvCallBinding
import com.bangkit.emergenz.ui.viewmodel.CallViewModel
import com.bangkit.emergenz.ui.viewmodel.LocViewModel
import com.bangkit.emergenz.ui.viewmodel.CallViewModelFactory
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
        val apiService = ApiConfig.getApiServiceCall()
        val callRepository = CallRepository(apiService)
        val callViewModelFactory = CallViewModelFactory(callRepository)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        callViewModel = ViewModelProvider(this, callViewModelFactory)[CallViewModel::class.java]
        locViewModel = ViewModelProvider(requireActivity())[LocViewModel::class.java]

        coroutineScope.launch {
            val waitSearch = async {
                setSearchPlace()
            }
            waitSearch.await()
            delay(500)
            getPlaceDetail()
            delay(500)
            setRecycleView()
        }
    }

    private fun setSearchPlace() {
        val myLoc= locViewModel.getArgumentLiveData().value.toString()
        val myLocBias= "circle%3A1000%40$myLoc"

        callViewModel.getPlaceId(query, myLocBias)
        callViewModel.getSearchText(myLoc, query)
    }

    private fun getPlaceDetail() {
        callViewModel.getListPlaceIds().observe(viewLifecycleOwner) { placeIds ->
            when (query) {
                FIRE -> {
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(FIRE, placeId)
                    }
                }
                HOSPITAL -> {
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(HOSPITAL, placeId)
                    }
                }
                POLICE -> {
                    for (placeId in placeIds) {
                        callViewModel.getPlaceDetail(POLICE, placeId)
                    }
                }
            }
        }
    }

    private fun setRecycleView() {
        val recyclerView = binding.rvCall
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = CallAdapter(emptyList(), requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        when (query) {
            FIRE -> {
                callViewModel.dataFire.observe(viewLifecycleOwner) { newData ->
                    adapter.setData(newData)
                }
            }
            HOSPITAL -> {
                callViewModel.dataHospital.observe(viewLifecycleOwner) { newData ->
                    adapter.setData(newData)
                }
            }
            POLICE -> {
                callViewModel.dataPolice.observe(viewLifecycleOwner) { newData ->
                    adapter.setData(newData)
                }
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
