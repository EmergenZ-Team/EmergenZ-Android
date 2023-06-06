package com.bangkit.emergenz.data.repository

import com.bangkit.emergenz.BuildConfig
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.api.CallApiService
import com.bangkit.emergenz.data.response.call.FindPlaceResponse
import com.bangkit.emergenz.data.response.call.PlaceDetailResponse
import com.bangkit.emergenz.data.response.call.SearchTextResponse
import retrofit2.Response

class CallRepository(private val callApiService: CallApiService) {

    private val apiKey = BuildConfig.API_KEY

    suspend fun fetchPlaceId(input: String, locationbias: String): Response<FindPlaceResponse> {
        val inputtype = "textquery"
        return ApiConfig.getApiServiceCall().findPlacesId(input, inputtype, locationbias, apiKey)
    }

    suspend fun fetchPlaceDetailsAndSave(placeId: String): Response<PlaceDetailResponse> {
        val field = "place_id,name,formatted_phone_number"
        return callApiService.placeDetail(field, placeId, apiKey)
    }

    suspend fun fetchSearchText(location: String, query: String): Response<SearchTextResponse> {
        val radius = "1000"
        return ApiConfig.getApiServiceCall().searchText(location, query, radius, apiKey)
    }

}