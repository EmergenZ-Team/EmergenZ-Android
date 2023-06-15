package com.bangkit.emergenz.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.emergenz.R
import com.bangkit.emergenz.adapter.ViewPageAdapter
import com.bangkit.emergenz.databinding.FragmentCallBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CallPageFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCallBinding? = null
    private val binding get() = _binding!!
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayoutView()
        coroutineScope.launch {
            delay(3000)
            dialog?.setCanceledOnTouchOutside(true)
        }
    }

    private fun setTabLayoutView() {
        val tabIcons = listOf(
            R.drawable.ic_tab_fire,
            R.drawable.ic_tab_police,
            R.drawable.ic_tab_hospital
        )
        val viewPager: ViewPager2 = binding.viewPager2Call
        val tabLayout: TabLayout = binding.tabLayoutCall

        val tabAdapter = ViewPageAdapter(childFragmentManager, lifecycle)

        viewPager.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

