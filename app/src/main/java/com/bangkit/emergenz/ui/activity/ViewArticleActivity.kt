package com.bangkit.emergenz.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.repository.ArticleRepository
import com.bangkit.emergenz.data.response.article.DataRecord
import com.bangkit.emergenz.databinding.ActivityViewArticleBinding
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModel
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModelFactory
import com.bumptech.glide.Glide

class ViewArticleActivity : AppCompatActivity() {
    private var _binding: ActivityViewArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.extras?.getString(EMAIL_KEY)
        val newsId = intent.extras?.getString(ID_KEY)
        val apiService = ApiConfigCloud.getApiService()
        val articleRepository = ArticleRepository(email!!, apiService)
        val articleViewModelFactory = ArticleViewModelFactory(articleRepository)

        articleViewModel = ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]
        articleViewModel.fetchDetail(newsId!!)
        articleViewModel.detailData.observe(this){detail->
            getDetail(detail)
        }
    }

    private fun getDetail(detailItem: DataRecord) {
        val reDate = Regex("(T|\\s)(([0-1][0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])(.*)")
        binding.apply {
            Glide.with(this@ViewArticleActivity)
                .load(detailItem.image)
                .into(ivDetailPhoto)
            tvDetailName.text = detailItem.title
            tvDetailDescription.text = detailItem.content
            tvDetailDate.text = detailItem.let { reDate.replace(it.uploadDate,"") }
        }
    }

    companion object {
        private const val EMAIL_KEY = "email_key"
        private const val ID_KEY = "id_key"

        fun newIntent(context: Context, email: String, id: String? = null): Intent {
            return Intent(context, ViewArticleActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(EMAIL_KEY, email)
                putExtra(ID_KEY, id)
            }
        }
    }
}