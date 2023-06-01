package com.bangkit.emergenz.data.api

import com.bangkit.emergenz.data.response.FindPlaceResponse
import com.bangkit.emergenz.data.response.PlaceDetailResponse
import com.bangkit.emergenz.data.response.SearchTextResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("maps/api/place/findplacefromtext/json")
    suspend fun findPlacesId(
        @Query("input") input: String,
        @Query("inputtype") inputtype: String,
        @Query("locationbias") locationbias: String,
        @Query("key") apiKey: String
    ): Response<FindPlaceResponse>

    @GET("maps/api/place/details/json")
    suspend fun placeDetail(
        @Query("fields") fields: String,
        @Query("place_id") place_id: String,
        @Query("key") apiKey: String
    ): Response<PlaceDetailResponse>

    @GET("maps/api/place/textsearch/json")
    suspend fun searchText(
        @Query("location") location: String,
        @Query("query") query: String,
        @Query("radius") radius: String,
        @Query("key") apiKey: String
    ): Response<SearchTextResponse>
}