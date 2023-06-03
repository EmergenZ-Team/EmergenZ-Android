package com.bangkit.emergenz.data.response.call

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchTextResponse(

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultsItem(

	@field:SerializedName("place_id")
	val placeId: String? = null,

) : Parcelable
