package com.bangkit.emergenz.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapter = parent.adapter ?: return
        val itemCount = adapter.itemCount
        val currentPosition = parent.getChildAdapterPosition(view)

        if (currentPosition == itemCount - 1) {
            outRect.bottom = spacing
        } else {
            outRect.bottom = 0
        }
    }
}