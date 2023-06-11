package com.bangkit.emergenz.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.emergenz.ui.fragment.RvCallFragment
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.FIRE
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.HOSPITAL
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.POLICE

class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val tabFragments = listOf(RvCallFragment(FIRE), RvCallFragment(POLICE), RvCallFragment(HOSPITAL))

    override fun getItemCount(): Int {
        return tabFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragments[position]
    }
}