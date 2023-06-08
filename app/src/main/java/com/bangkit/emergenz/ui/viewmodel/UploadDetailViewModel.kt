package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.response.RegisterDetailResponse
import com.bangkit.emergenz.util.reduceFileImage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadDetailViewModel(private val pref: UserPreferences) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished
    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private fun uploadStory(
        email: RequestBody,
        full_name: RequestBody,
        nik: RequestBody,
        gender: RequestBody,
        province: RequestBody,
        city: RequestBody,
        address: RequestBody,
        requestImageFile: MultipartBody.Part
    ) {
        _isLoading.value = true
        val client = ApiConfigCloud.getApiService().updateDetailProfile(
            email,
            full_name,
            nik,
            gender,
            province,
            city,
            address,
            requestImageFile
        )
        client.enqueue(object : Callback<RegisterDetailResponse> {
            override fun onResponse(
                call: Call<RegisterDetailResponse>,
                response: Response<RegisterDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _toast.value = responseBody.message
                        _isFinished.value = true
                    } else {
                        _toast.value = response.message()
                        _isFinished.value = true
                    }
                } else {
                    _toast.value = response.message()
                }
            }

            override fun onFailure(call: Call<RegisterDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = t.message
            }
        })
    }

    fun getEmail(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun reduceFileSize(file : File) : File {
        return reduceFileImage(file)
    }

    fun uploadStoryIntent(
        email: RequestBody,
        full_name: RequestBody,
        nik: RequestBody,
        gender: RequestBody,
        province: RequestBody,
        city: RequestBody,
        address: RequestBody,
        requestImageFile: MultipartBody.Part
    ) {
        uploadStory(email, full_name, nik, gender, province, city, address, requestImageFile)
    }
}