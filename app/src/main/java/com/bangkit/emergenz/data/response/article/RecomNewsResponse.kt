package com.bangkit.emergenz.data.response.article

import com.google.gson.annotations.SerializedName

data class RecomNewsResponse(

	@field:SerializedName("data")
	val data: List<DataRecom>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataRecom(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("news_id")
	val newsId: Int
)
