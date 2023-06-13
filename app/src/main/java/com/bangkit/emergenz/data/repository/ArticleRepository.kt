package com.bangkit.emergenz.data.repository

import com.bangkit.emergenz.data.api.ApiServiceCloud
import com.bangkit.emergenz.data.response.article.AddRecordResponse
import com.bangkit.emergenz.data.response.article.RecomNewsResponse
import retrofit2.Response

class ArticleRepository(private val email: String, private val articleApiService: ApiServiceCloud) {
    suspend fun fetchDataAndCache(): Response<RecomNewsResponse> {
        return articleApiService.findArticle(email)
    }
    suspend fun fetchDetail(news_id: String): Response<AddRecordResponse> {
        return articleApiService.getDetailArticle(news_id ,email)
    }

}