package com.rapidops.salesmatechatsdk.app.view
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesItemDecoration @JvmOverloads constructor(
    private val space: Int,
    private val addSpaceAboveFirstItem: Boolean = DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM,
    private val addSpaceBelowLastItem: Boolean = DEFAULT_ADD_SPACE_BELOW_LAST_ITEM
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (space <= 0) {
            return
        }
        if (addSpaceAboveFirstItem && parent.getChildLayoutPosition(view) < 1
            || parent.getChildLayoutPosition(view) >= 1
        ) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = space
            } else {
                outRect.left = space
            }
        }
        if (addSpaceBelowLastItem
            && parent.getChildAdapterPosition(view) == getTotalItemCount(parent) - 1
        ) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.bottom = space
            } else {
                outRect.right = space
            }
        }
    }

    private fun getTotalItemCount(parent: RecyclerView): Int {
        return parent.adapter!!.itemCount
    }

    private fun getOrientation(parent: RecyclerView): Int {
        return if (parent.layoutManager is LinearLayoutManager) {
            (parent.layoutManager as LinearLayoutManager?)!!.orientation
        } else {
            throw IllegalStateException(
                "SpaceItemDecoration can only be used with a LinearLayoutManager."
            )
        }
    }

    companion object {
        private const val DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM = false
        private const val DEFAULT_ADD_SPACE_BELOW_LAST_ITEM = false
    }
}