package com.bangkit.emergenz.data.response.call

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FindPlaceResponse(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class CandidatesItem(

	@field:SerializedName("place_id")
	val placeId: String? = null
) : Parcelable
