package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.response.LoginResponse
import com.bangkit.emergenz.util.extractErrorMessageFromJson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private fun postLogin(email : String, password : String) {
        _isLoading.value = true
        val client = ApiConfigCloud.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        if (!responseBody.error){
                            val token = responseBody.data.token
                            val username = responseBody.data.name
                            _toast.value = responseBody.message
                            ApiConfigCloud.setToken(token)
                            saveToken(token)
                            setUser(username, email)
                            setSession(true)
                            _isFinished.value = true
                        }else{
                            _toast.value = responseBody.message
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractErrorMessageFromJson(errorBody)
                    _toast.value = errorMessage
                }
            }

            override fun onFailure(call : Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _toast.value = t.message
            }
        })
    }
    fun saveToken(Token: String) {
        viewModelScope.launch {
            pref.saveToken(Token)
        }
    }

    fun setSession(Session: Boolean) {
        viewModelScope.launch {
            pref.setSession(Session)
        }
    }

    fun setUser(Username: String, Email : String) {
        viewModelScope.launch {
            pref.setUser(Username)
            pref.setEmail(Email)
        }
    }

    fun postLoginIntent(email: String, password: String){
        postLogin(email, password)
    }
}