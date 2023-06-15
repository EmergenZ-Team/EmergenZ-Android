package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bangkit.emergenz.R
import com.bangkit.emergenz.adapter.ArticleAdapter
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.repository.ArticleRepository
import com.bangkit.emergenz.databinding.FragmentArticleBinding
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModel
import com.bangkit.emergenz.ui.viewmodel.ArticleViewModelFactory
import com.bangkit.emergenz.ui.viewmodel.ProfileViewModel
import com.bangkit.emergenz.ui.viewmodel.ViewModelFactory
import com.bangkit.emergenz.util.LastItemSpacingDecoration
import com.bangkit.emergenz.util.animateVisibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
        setToolbar()
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        var valueEmail =""
        profileViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[ProfileViewModel::class.java]
        profileViewModel.getEmail().observe(viewLifecycleOwner){email ->
            valueEmail = email
        }
        coroutineScope.launch {
            delay(500)
            getArticle(valueEmail)
            swipeUpLayout()
        }

    }

    private fun getArticle(email: String){
        val apiService = ApiConfigCloud.getApiService()
        val articleRepository = ArticleRepository(email, apiService)
        val articleViewModelFactory = ArticleViewModelFactory(articleRepository)

        articleViewModel = ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]
        articleViewModel.chckErr.observe(viewLifecycleOwner){check->
            loadError(check)
        }
        articleViewModel.fetchDataAndCache()
        articleViewModel.isLoading.observe(viewLifecycleOwner){load ->
            showLoading(load)
        }
        setDataToFragment(email)
    }

    private fun setToolbar(){
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = getString(R.string.label_article)
    }

    private fun setDataToFragment(email: String) {
        val recyclerView = binding.rvArticle
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArticleAdapter(email, emptyList(), requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        articleViewModel.cachedData.observe(viewLifecycleOwner) { newData ->
            adapter.setData(newData)
        }
        val itemDecoration = LastItemSpacingDecoration(290)
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

    private fun loadError(error: Boolean){
        if (error){
            articleViewModel.txtErr.observe(viewLifecycleOwner){txtErr->
                val reTxt = Regex("=([^\"\\\\)]+)")
                val matchResult = reTxt.find(txtErr)
                val result = matchResult?.groupValues?.get(1)?.trim()
                binding.errorMsg.text = result
            }
            binding.layoutError.visibility = View.VISIBLE
            binding.retryButton.setOnClickListener {
                lifecycleScope.launch {
                    binding.layoutError.visibility = View.GONE
                    showLoading(true)
                    delay(1000)
                    articleViewModel.retryButton()
                }
            }
        }else{
            binding.layoutError.visibility = View.GONE
        }

    }

    private fun swipeUpLayout(){
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                binding.layoutError.visibility = View.GONE
                showLoading(true)
                delay(1000)
                articleViewModel.retryButton()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}