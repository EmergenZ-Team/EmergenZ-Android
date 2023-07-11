package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.repository.HistoryRepository
import com.bangkit.emergenz.data.response.history.DataItem
import com.bangkit.emergenz.data.response.history.GetHistoryResponse
import com.bangkit.emergenz.util.ApiResult
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val pref: UserPreferences
) : ViewModel() {

    private val _txtErr = MutableLiveData<String?>()
    val txtErr: LiveData<String?> = _txtErr
    private val _chckErr = MutableLiveData<Boolean>()
    val chckErr: LiveData<Boolean> = _chckErr
    private val _listHistory: MutableSet<DataItem> = mutableSetOf()
    val listHistory: MutableLiveData<List<DataItem>> = MutableLiveData()

    private fun addHistory(articleId: DataItem) {
        if (_listHistory.add(articleId)) {
            listHistory.value = _listHistory.toList()
        }
    }

    fun saveHistory(name: String, instancename: String, phonenumber: String) {
        viewModelScope.launch {
            val currentDate = Date()
            val locale = Locale("id", "ID")
            val timeFormat = SimpleDateFormat("HH:mm", locale)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", locale)

            val date = dateFormat.format(currentDate)
            val time = timeFormat.format(currentDate)

            try {
                val response = historyRepository.saveHistory(name, instancename,phonenumber, time, date)
                if (response.isSuccessful) {
                    val saveResponse = response.body()
                    val results = saveResponse?.phonenumber
                    if (results != null) {
                        _txtErr.value = "Success"
                    }
                } else {
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            } catch (e: Exception) {
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
            }
        }
    }

    fun getHistory(name: String) {
        viewModelScope.launch {
            try {
                val response = historyRepository.fetchHistory(name)
                if (response.isSuccessful){
                    _chckErr.value = false
                    val getResponse = response.body()
                    val results = getResponse?.data
                    if (results != null) {
                        for (article in results) {
                            if (article != null) {
                                val gson = Gson()
                                val jsonResponse = gson.toJson(getResponse)
                                val responseSort = gson.fromJson(jsonResponse, GetHistoryResponse::class.java)
                                val data = responseSort.data
                                val sortedData = data?.sortedByDescending { getDateAndTime(it!!) }
                                if (sortedData != null) {
                                    for (item in sortedData) {
                                        if (item != null) {
                                            addHistory(item)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else {
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e: Exception) {
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
            }
        }
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    private fun getDateAndTime(data: DataItem): Date {
        val dateTimeString = "${data.date} ${data.time}"
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale("id", "ID"))
        return format.parse(dateTimeString.replace(".", ":")) ?: Date()
    }
}


@Suppress("UNCHECKED_CAST")
class HistoryViewModelFactory(
    private val historyRepository: HistoryRepository,
    private val pref: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(historyRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}