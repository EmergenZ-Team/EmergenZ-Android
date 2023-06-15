package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.local.model.CallUrgent
import com.bangkit.emergenz.data.repository.CallRepository
import com.bangkit.emergenz.data.response.call.Result
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.FIRE
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.HOSPITAL
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.POLICE
import com.bangkit.emergenz.util.ApiResult
import com.bangkit.emergenz.util.LimitedSizeList
import kotlinx.coroutines.launch

class CallViewModel(private val callRepository: CallRepository) : ViewModel(){

    private val uniquePlaceIdsF: LimitedSizeList<String> = LimitedSizeList(2)
    private val listPlaceIdsF: MutableLiveData<List<String>> = MutableLiveData()
    private val uniquePlaceIdsP: LimitedSizeList<String> = LimitedSizeList(2)
    private val listPlaceIdsP: MutableLiveData<List<String>> = MutableLiveData()
    private val uniquePlaceIdsH: LimitedSizeList<String> = LimitedSizeList(2)
    private val listPlaceIdsH: MutableLiveData<List<String>> = MutableLiveData()

    private val _chckErr = MutableLiveData<Boolean>()
    private val _txtErr = MutableLiveData<String>()

    init {
        listPlaceIdsF.value = uniquePlaceIdsF.toList()
        listPlaceIdsP.value = uniquePlaceIdsP.toList()
        listPlaceIdsH.value = uniquePlaceIdsH.toList()
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

    val dataUrgentPolice: MutableLiveData<List<CallUrgent>> = MutableLiveData()
    val dataUrgentFire: MutableLiveData<List<CallUrgent>> = MutableLiveData()
    val dataUrgentHospital: MutableLiveData<List<CallUrgent>> = MutableLiveData()

    fun setDataUrgent() {
        viewModelScope.launch {
            dataUrgentPolice.value = callRepository.fetchCallUrgent(POLICE)
            dataUrgentHospital.value = callRepository.fetchCallUrgent(HOSPITAL)
            dataUrgentFire.value = callRepository.fetchCallUrgent(FIRE)
        }
    }

    fun getListPlaceIdsF(): LiveData<List<String>> {
        return listPlaceIdsF
    }

    private fun addPlaceIdF(placeId: String) {
        if (uniquePlaceIdsF.add(placeId)) {
            listPlaceIdsF.value = uniquePlaceIdsF.toList()
        }
    }

    fun getListPlaceIdsP(): LiveData<List<String>> {
        return listPlaceIdsP
    }

    private fun addPlaceIdP(placeId: String) {
        if (uniquePlaceIdsP.add(placeId)) {
            listPlaceIdsP.value = uniquePlaceIdsP.toList()
        }
    }

    fun getListPlaceIdsH(): LiveData<List<String>> {
        return listPlaceIdsH
    }

    private fun addPlaceIdH(placeId: String) {
        if (uniquePlaceIdsH.add(placeId)) {
            listPlaceIdsH.value = uniquePlaceIdsH.toList()
        }
    }

    fun getPlaceId(query: String, locationbias: String) {
        viewModelScope.launch {
            try {
                val response = callRepository.fetchPlaceId(query, locationbias)
                if (response.isSuccessful) {
                    _chckErr.value = false
                    val placesResponse = response.body()
                    val results = placesResponse?.candidates
                    if (results != null) {
                        for (place in results) {
                            if (place != null) {
                                when (query) {
                                    POLICE -> (
                                            addPlaceIdP(place.placeId!!)
                                            )
                                    FIRE -> (
                                            addPlaceIdF(place.placeId!!)
                                            )
                                    HOSPITAL -> (
                                            addPlaceIdH(place.placeId!!)
                                            )
                                }
                            }
                        }
                    }
                } else {
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e:Exception){
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
            }
        }
    }

    fun getPlaceDetail(query: String, placeId: String) {
        viewModelScope.launch {
            try {
                val response = callRepository.fetchPlaceDetailsAndSave(placeId)
                if (response.isSuccessful) {
                    _chckErr.value = false
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
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e:Exception){
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
            }

        }
    }

    fun getSearchText(location: String, query: String) {
        viewModelScope.launch {
            try {
                val response = callRepository.fetchSearchText(location, query)
                if (response.isSuccessful) {
                    _chckErr.value = false
                    val searchResponse = response.body()
                    val results = searchResponse?.results
                    if (results != null) {
                        for (place in results) {
                            if (place != null) {
                                when (query) {
                                    POLICE -> (
                                            addPlaceIdP(place.placeId!!)
                                            )
                                    FIRE -> (
                                            addPlaceIdF(place.placeId!!)
                                            )
                                    HOSPITAL -> (
                                            addPlaceIdH(place.placeId!!)
                                            )
                                }
                            }
                        }
                    }
                } else {
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e:Exception){
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
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
