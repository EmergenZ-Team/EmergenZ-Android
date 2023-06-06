package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreferences) : ViewModel() {
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
}