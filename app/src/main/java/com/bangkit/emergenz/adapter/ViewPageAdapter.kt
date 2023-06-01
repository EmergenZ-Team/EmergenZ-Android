package com.bangkit.emergenz.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.emergenz.ui.fragment.RvCallFragment
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.FIRE
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.HOSPITAL
import com.bangkit.emergenz.ui.fragment.RvCallFragment.Companion.POLICE

class ViewPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RvCallFragment(FIRE)
            1 -> RvCallFragment(HOSPITAL)
            2 -> RvCallFragment(POLICE)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}