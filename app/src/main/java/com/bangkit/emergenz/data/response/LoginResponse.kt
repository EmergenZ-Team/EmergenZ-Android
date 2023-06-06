package com.bangkit.emergenz.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("token")
	val token: String
) : Parcelable
