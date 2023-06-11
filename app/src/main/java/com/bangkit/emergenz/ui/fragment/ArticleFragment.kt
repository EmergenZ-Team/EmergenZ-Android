package com.bangkit.emergenz.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.adapter.ArticleAdapter
import com.bangkit.emergenz.data.api.ApiConfig
import com.bangkit.emergenz.data.database.ArticleDatabase
import com.bangkit.emergenz.data.repository.ArticleRepository
import com.bangkit.emergenz.databinding.FragmentArticleBinding
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModel
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModelFactory
import com.bangkit.emergenz.util.LastItemSpacingDecoration
import com.bangkit.emergenz.util.animateVisibility

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        val articleApiService = ApiConfig.getApiServiceArticle()
        val articleDao = ArticleDatabase.getInstance(requireContext()).articleDao()
        val articleRepository = ArticleRepository(articleApiService, articleDao)
        val articleViewModelFactory = ArticleViewModelFactory(articleRepository)

        articleViewModel = ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]
        setToolbar()
        articleViewModel.fetchDataAndCache()
        setDataToFragment()
    }

    private fun setToolbar(){
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = "Artikel"
    }

    private fun setDataToFragment() {
        val recyclerView = binding.rvArticle
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArticleAdapter(emptyList(), requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        articleViewModel.cachedData.observe(viewLifecycleOwner) { newData ->
            adapter.setData(newData)
            Log.d("Artikel", "$newData")
            showLoading(false)
        }
        val itemDecoration = LastItemSpacingDecoration(250)
        recyclerView.addItemDecoration(itemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            rvArticle.isEnabled = !isLoading

            // Animate views alpha
            if (isLoading) {
                loadingBar2.animateVisibility(true)
            } else {
                loadingBar2.animateVisibility(false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}