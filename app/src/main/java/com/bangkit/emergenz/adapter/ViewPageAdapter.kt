package com.bangkit.emergenz.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.emergenz.ui.fragment.RvCallFragment

class ViewPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RvCallFragment()
            1 -> RvCallFragment()
            2 -> RvCallFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}