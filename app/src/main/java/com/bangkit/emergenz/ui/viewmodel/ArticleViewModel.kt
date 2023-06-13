package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.repository.ArticleRepository
import com.bangkit.emergenz.data.response.article.DataRecom
import com.bangkit.emergenz.data.response.article.DataRecord
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _cachedData : MutableSet<DataRecom> = mutableSetOf()
    val cachedData: MutableLiveData<List<DataRecom>> = MutableLiveData()

    private val _detailData = MutableLiveData<DataRecord>()
    val detailData: MutableLiveData<DataRecord> = _detailData

    private fun addPlaceId(articleId: DataRecom) {
        if (_cachedData.add(articleId)) {
            cachedData.value = _cachedData.toList()
        }
    }

    fun fetchDataAndCache() {
        viewModelScope.launch {
            viewModelScope.launch {
                val response = articleRepository.fetchDataAndCache()
                if (response.isSuccessful) {
                    val articleResponse = response.body()
                    val results = articleResponse?.data
                    if (results != null) {
                        for (article in results) {
                            addPlaceId(article)
                        }
                    }
                } else {
                    // Handle the error
                }
            }
        }
    }

    fun fetchDetail(news_id: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                val response = articleRepository.fetchDetail(news_id)
                if (response.isSuccessful) {
                    val articleResponse = response.body()
                    val results = articleResponse?.data
                    if (results != null) {
                        for (article in results) {
                            _detailData.value = article
                        }
                    }
                } else {
                    // Handle the error
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ArticleViewModelFactory(private val articleRepository: ArticleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(articleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}