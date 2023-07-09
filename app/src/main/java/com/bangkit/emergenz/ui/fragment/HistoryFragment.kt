package com.bangkit.emergenz.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.emergenz.R
import com.bangkit.emergenz.adapter.HistoryAdapter
import com.bangkit.emergenz.data.api.ApiConfigCloud
import com.bangkit.emergenz.data.local.datastore.UserPreferences
import com.bangkit.emergenz.data.repository.HistoryRepository
import com.bangkit.emergenz.data.response.history.DataItem
import com.bangkit.emergenz.databinding.FragmentHistoryBinding
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModel
import com.bangkit.emergenz.ui.viewmodel.HistoryViewModelFactory
import com.bangkit.emergenz.util.LastItemSpacingDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        val apiService = ApiConfigCloud.getApiService()
        val historyRepository = HistoryRepository(apiService)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        var data = emptyList<DataItem>()

        historyViewModel = ViewModelProvider(requireActivity(), HistoryViewModelFactory(historyRepository, pref))[HistoryViewModel::class.java]
        historyViewModel.getName().observe(viewLifecycleOwner){name->
            historyViewModel.getHistory(name)
        }
        historyViewModel.listHistory.observe(viewLifecycleOwner){newData->
            data = newData
        }
        historyViewModel.txtErr.observe(viewLifecycleOwner){
            Log.d("DATA", "$it")
        }
        coroutineScope.launch{
            delay(2000)
            setDataToFragment(data)
        }
    }

    private fun setToolbar(){
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()
        activity.supportActionBar?.title = getString(R.string.label_history)
    }

    private fun setDataToFragment(data: List<DataItem>) {
        historyViewModel.getName().observe(viewLifecycleOwner) { name ->
            val recyclerView = binding.rvHistory
            val layoutManager = LinearLayoutManager(requireContext())
            val adapter = HistoryAdapter(data, historyViewModel, name, requireContext())
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            val itemDecoration = LastItemSpacingDecoration(290)
            recyclerView.addItemDecoration(itemDecoration)
        }
    }


}