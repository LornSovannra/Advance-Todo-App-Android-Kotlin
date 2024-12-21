package com.frodzy.todo_list.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Separator {
    class HorizontalItemSeparator(context: Context, space: Int = 16) : RecyclerView.ItemDecoration(){
        private val spaceInDp = Converter.dpToPx(context, space)

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = spaceInDp

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = spaceInDp
            }
        }
    }

    class VerticalItemSeparator(context: Context, space: Int = 16) : RecyclerView.ItemDecoration(){
        private val spaceInDp = Converter.dpToPx(context, space)

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = spaceInDp
            outRect.bottom = spaceInDp
            outRect.left = spaceInDp

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = spaceInDp
            }
        }
    }
}