package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocViewModel : ViewModel() {
    private val argumentLiveData = MutableLiveData<String>()

    fun setArgument(argument: String) {
        argumentLiveData.value = argument
    }

    fun getArgumentLiveData(): LiveData<String> = argumentLiveData
}