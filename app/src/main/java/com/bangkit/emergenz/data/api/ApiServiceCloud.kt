package com.bangkit.emergenz.data.api

import com.bangkit.emergenz.data.response.GetDetailResponse
import com.bangkit.emergenz.data.response.LoginResponse
import com.bangkit.emergenz.data.response.RegisterDetailResponse
import com.bangkit.emergenz.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiServiceCloud {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @Multipart
    @POST("add_detail_user")
    fun updateDetailProfile(
        @Part("email") email: RequestBody,
        @Part("name") fullName : RequestBody,
        @Part("nik") nik : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("province") province : RequestBody,
        @Part("city") city : RequestBody,
        @Part("address") address : RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<RegisterDetailResponse>

    @GET("user_data/{email}")
    fun getDetailUser(
        @Path("email") email : String
    ) : Call<GetDetailResponse>
}