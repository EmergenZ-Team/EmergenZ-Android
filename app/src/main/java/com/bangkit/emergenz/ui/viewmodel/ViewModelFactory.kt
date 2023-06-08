package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.emergenz.data.local.datastore.UserPreferences

class ViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TokenViewModel::class.java)) {
            TokenViewModel(pref) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(pref) as T
        }else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel(pref) as T
        }else if (modelClass.isAssignableFrom(UploadDetailViewModel::class.java)) {
            UploadDetailViewModel(pref) as T
        }else
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}