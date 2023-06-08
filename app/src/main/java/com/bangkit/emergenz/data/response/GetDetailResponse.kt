package com.bangkit.emergenz.data.response

import com.google.gson.annotations.SerializedName

data class GetDetailResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: DataUser? = null,
)

data class DataUser(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("address")
	val address: String? = null
)
