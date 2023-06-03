package com.bangkit.emergenz.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: Any? = null,

    @field:SerializedName("count")
    val count: Int? = null,

    @field:SerializedName("results")
    val results: List<CachedArticle?>? = null
)

@Entity(tableName = "cache_table")
data class CachedArticle(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    )