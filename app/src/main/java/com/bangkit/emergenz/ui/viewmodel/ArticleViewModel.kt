package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.local.model.CachedArticle
import com.bangkit.emergenz.data.repository.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _cachedData : MutableSet<CachedArticle> = mutableSetOf()
    val cachedData: MutableLiveData<List<CachedArticle>> = MutableLiveData()

    private fun addPlaceId(articleId: CachedArticle) {
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
                    val results = articleResponse?.results
                    if (results != null) {
                        for (article in results) {
                            if (article != null) {
                                addPlaceId(article)
                            }
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