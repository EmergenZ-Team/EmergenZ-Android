package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.data.response.Result
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.FIRE
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.HOSPITAL
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.POLICE
import kotlinx.coroutines.launch

class CallViewModel(private val callRepository: CallRepository) : ViewModel(){

    private val uniquePlaceIds: MutableSet<String> = mutableSetOf()
    private val listPlaceIds: MutableLiveData<List<String>> = MutableLiveData()

    init {
        listPlaceIds.value = uniquePlaceIds.toList()
    }

    private val _dataPolice: MutableSet<Result> = mutableSetOf()
    val dataPolice: MutableLiveData<List<Result>> = MutableLiveData()

    private fun setDataPolice(data: Result) {
        if (_dataPolice.add(data)) {
            dataPolice.value = _dataPolice.toList()
        }
    }

    private val _dataFire: MutableSet<Result> = mutableSetOf()
    val dataFire: MutableLiveData<List<Result>> = MutableLiveData()

    private fun setDataFire(data: Result) {
        if (_dataFire.add(data)) {
            dataFire.value = _dataFire.toList()
        }
    }

    private val _dataHospital: MutableSet<Result> = mutableSetOf()
    val dataHospital: MutableLiveData<List<Result>> = MutableLiveData()

    private fun setDataHospital(data: Result) {
        if (_dataHospital.add(data)) {
            dataHospital.value = _dataHospital.toList()
        }
    }

    fun getListPlaceIds(): LiveData<List<String>> {
        return listPlaceIds
    }

    private fun addPlaceId(placeId: String) {
        if (uniquePlaceIds.add(placeId)) {
            listPlaceIds.value = uniquePlaceIds.toList()
        }
    }

    fun getPlaceId(input: String, locationbias: String) {
        viewModelScope.launch {
            val response = callRepository.fetchPlaceId(input, locationbias)
            if (response.isSuccessful) {
                val placesResponse = response.body()
                val results = placesResponse?.candidates
                if (results != null) {
                    for (place in results) {
                        if (place != null) {
                            addPlaceId(place.placeId!!)
                        }
                    }
                }
            } else {
                // Handle the error
            }
        }
    }

    fun getPlaceDetail(query: String, placeId: String) {
        viewModelScope.launch {
            val response = callRepository.fetchPlaceDetailsAndSave(placeId)
            if (response.isSuccessful) {
                val placeDetails = response.body()?.result
                placeDetails?.let {
                    if (placeDetails.formattedPhoneNumber != null){
                        val place = Result(
                            placeDetails.name,
                            placeDetails.place_id,
                            placeDetails.formattedPhoneNumber
                        )
                        when (query) {
                            POLICE -> (
                                    setDataPolice(place)
                                    )
                            FIRE -> (
                                    setDataFire(place)
                                    )
                            HOSPITAL -> (
                                    setDataHospital(place)
                                    )
                        }
                    }
                }
            } else {
                // Handle the API error
            }
        }
    }

    fun getSearchText(location: String, query: String) {
        viewModelScope.launch {
            val response = callRepository.fetchSearchText(location, query)
            if (response.isSuccessful) {
                val searchResponse = response.body()
                val results = searchResponse?.results
                if (results != null) {
                    for (place in results) {
                        if (place != null) {
                            addPlaceId(place.placeId!!)
                        }
                    }
                }
            } else {
                // Handle the error
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CallViewModelFactory(private val callRepository: CallRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallViewModel::class.java)) {
            return CallViewModel(callRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
