package com.bangkit.emergenz.data.api

import com.bangkit.emergenz.data.local.model.ArticleResponse

import retrofit2.Response
import retrofit2.http.GET

interface ArticleApiService {
    @GET("v4/articles/?format=json&limit=10")
    suspend fun findArticle(
    ): Response<ArticleResponse>
    
}