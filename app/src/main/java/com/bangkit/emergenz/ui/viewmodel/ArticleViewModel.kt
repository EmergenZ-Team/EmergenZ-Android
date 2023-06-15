package com.bangkit.emergenz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bangkit.emergenz.data.repository.ArticleRepository
import com.bangkit.emergenz.data.response.article.DataRecom
import com.bangkit.emergenz.data.response.article.DataRecord
import com.bangkit.emergenz.util.ApiResult
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _cachedData : MutableSet<DataRecom> = mutableSetOf()
    val cachedData: MutableLiveData<List<DataRecom>> = MutableLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailData = MutableLiveData<DataRecord>()
    val detailData: MutableLiveData<DataRecord> = _detailData

    private val _chckErr = MutableLiveData<Boolean>()
    val chckErr: LiveData<Boolean> = _chckErr
    private val _txtErr = MutableLiveData<String>()
    val txtErr: LiveData<String> = _txtErr

    private fun addPlaceId(articleId: DataRecom) {
        if (_cachedData.add(articleId)) {
            cachedData.value = _cachedData.toList()
        }
    }

    fun retryButton(){
        _cachedData.clear()
        _isLoading.value = true
        return fetchDataAndCache()
    }

    fun fetchDataAndCache() {
        viewModelScope.launch {
            try {
                val response = articleRepository.fetchDataAndCache()
                if (response.isSuccessful) {
                    _chckErr.value = false
                    _isLoading.value = false
                    val articleResponse = response.body()
                    val results = articleResponse?.data
                    if (results != null) {
                        for (article in results) {
                            addPlaceId(article)
                        }
                    }
                } else {
                    _isLoading.value = false
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e:Exception){
                _isLoading.value = false
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
            }
        }
    }

    fun fetchDetail(news_id: String) {
        viewModelScope.launch {
            try {
                val response = articleRepository.fetchDetail(news_id)
                if (response.isSuccessful) {
                    _chckErr.value = false
                    _isLoading.value = false
                    val articleResponse = response.body()
                    val results = articleResponse?.data
                    if (results != null) {
                        for (article in results) {
                            _detailData.value = article
                        }
                    }
                } else {
                    _isLoading.value = false
                    _chckErr.value = true
                    _txtErr.value = ApiResult.Error("Error:${response.code()}").toString()
                }
            }catch (e:Exception){
                _isLoading.value = false
                _chckErr.value = true
                _txtErr.value = ApiResult.Error(e.message.toString()).toString()
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