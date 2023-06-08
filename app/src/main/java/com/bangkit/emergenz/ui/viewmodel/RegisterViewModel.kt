package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.response.RegisterResponse
import com.bangkit.emergenz.util.extractErrorMessageFromJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    companion object{
    }

    private fun postRegister(username: String,email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfigCloud.getApiService().postRegister(username ,email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _toast.value = responseBody.message
                        _isFinished.value = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractErrorMessageFromJson(errorBody)
                    _toast.value = errorMessage
                }
            }

            override fun onFailure(call : Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = t.message
            }
        })
    }

    fun postRegisterIntent(username: String, email: String, password: String){
        postRegister(username, email, password)
    }
}