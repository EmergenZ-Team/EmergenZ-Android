package com.bangkit.emergenz.data.api

import com.bangkit.emergenz.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiServiceCall(): CallApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(CallApiService::class.java)
        }
    }
}