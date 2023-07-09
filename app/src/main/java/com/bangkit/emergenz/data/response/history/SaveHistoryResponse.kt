package com.bangkit.emergenz.data.response.history

import com.google.gson.annotations.SerializedName

data class SaveHistoryResponse(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("instancename")
	val instancename: String? = null,

	@field:SerializedName("phonenumber")
	val phonenumber: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
