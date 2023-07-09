package com.bangkit.emergenz.data.response.history

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("instancename")
	val instancename: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phonenumber")
	val phonenumber: String? = null,

	@field:SerializedName("time")
	val time: String? = null
)
