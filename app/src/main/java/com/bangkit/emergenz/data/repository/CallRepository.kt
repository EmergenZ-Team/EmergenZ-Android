package com.bangkit.emergenz.data.repository

import com.bangkit.emergenz.BuildConfig
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.api.ApiService
import com.bangkit.emergenz.data.response.FindPlaceResponse
import com.bangkit.emergenz.data.response.PlaceDetailResponse
import com.bangkit.emergenz.data.response.SearchTextResponse
import retrofit2.Response

class CallRepository(private val apiService: ApiService) {

    private val apiKey = BuildConfig.API_KEY

    suspend fun fetchPlaceId(input: String, locationbias: String): Response<FindPlaceResponse> {
        val inputtype = "textquery"
        return ApiConfig.getApiService().findPlacesId(input, inputtype, locationbias, apiKey)
    }

    suspend fun fetchPlaceDetailsAndSave(placeId: String): Response<PlaceDetailResponse> {
        val field = "place_id,name,formatted_phone_number"
        return apiService.placeDetail(field, placeId, apiKey)
    }

    suspend fun fetchSearchText(location: String, query: String): Response<SearchTextResponse> {
        val radius = "1000"
        return ApiConfig.getApiService().searchText(location, query, radius, apiKey)
    }

}