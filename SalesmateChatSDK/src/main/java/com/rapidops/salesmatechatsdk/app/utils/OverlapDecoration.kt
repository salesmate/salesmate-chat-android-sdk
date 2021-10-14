package com.rapidops.salesmatechatsdk.app.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OverlapDecoration() : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        outRect[0, 0, VERT_OVERLAP] = 0
    }

    companion object {
        const val VERT_OVERLAP = -40
    }
}