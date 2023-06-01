package com.bangkit.emergenz.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlaceDetailResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class Result(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("place_id")
	val place_id: String,

	@field:SerializedName("formatted_phone_number")
	val formattedPhoneNumber: String? = null
) : Parcelable
