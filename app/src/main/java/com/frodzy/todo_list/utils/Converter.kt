package com.frodzy.todo_list.utils

import android.content.Context

class Converter {
    companion object {
        fun dpToPx(context: Context, dp: Int): Int {
            return (dp * context.resources.displayMetrics.density).toInt()
        }
    }
}