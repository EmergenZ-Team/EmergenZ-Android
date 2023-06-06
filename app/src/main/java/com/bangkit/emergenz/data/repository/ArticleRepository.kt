package com.bangkit.emergenz.data.repository

import com.bangkit.emergenz.data.api.ArticleApiService
import com.bangkit.emergenz.data.dao.ArticleDao
import com.bangkit.emergenz.data.local.model.ArticleResponse
import retrofit2.Response

class ArticleRepository(private val articleApiService: ArticleApiService, private val articleDao: ArticleDao) {
    suspend fun fetchDataAndCache(): Response<ArticleResponse> {
        return articleApiService.findArticle()
    }
}