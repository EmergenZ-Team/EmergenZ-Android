package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.emergenz.data.local.datastore.UserPreferences

class TokenViewModel(private val pref: UserPreferences) : ViewModel() {
    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun getSession(): LiveData<Boolean> {
        return pref.getSession().asLiveData()
    }
}