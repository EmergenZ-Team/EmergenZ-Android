package com.bangkit.emergenz.data.repository

import com.bangkit.emergenz.data.api.ApiServiceCloud
import com.bangkit.emergenz.data.response.history.GetHistoryResponse
import com.bangkit.emergenz.data.response.history.SaveHistoryResponse
import retrofit2.Response

class HistoryRepository(private val historyApiService: ApiServiceCloud) {
    suspend fun saveHistory(name : String, instancename: String, phonenumber: String, time: String, date: String): Response<SaveHistoryResponse>{
        return historyApiService.saveHistory(name, instancename, phonenumber, time, date)
    }

    suspend fun fetchHistory(name: String): Response<GetHistoryResponse>{
        return historyApiService.getHistory(name)
    }
}