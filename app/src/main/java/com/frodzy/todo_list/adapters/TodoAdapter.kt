package com.frodzy.todo_list.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frodzy.todo_list.R
import com.frodzy.todo_list.activities.ViewToDoActivity
import com.frodzy.todo_list.models.Todo
import com.frodzy.todo_list.view_models.TodoViewModel

class TodoAdapter(private val todoViewModel: TodoViewModel): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val btnToggleComplete: ImageView = itemView.findViewById(R.id.btnToggleComplete)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Todo>(){
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_row_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val todo = differ.currentList[position]

        holder.apply {
            title.text = todo.title

            if(todo.isCompleted){
                btnToggleComplete.setImageResource(R.drawable.baseline_radio_button_checked_24)
            } else {
                btnToggleComplete.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
            }

            btnToggleComplete.setOnClickListener {
                todoViewModel.toggleIsCompleted(todo.id, !todo.isCompleted)
            }

            itemView.apply {
                setOnClickListener {
                    val viewToDoIntent = Intent(context, ViewToDoActivity::class.java)
                    viewToDoIntent.putExtra("TODO", todo)
                    context.startActivity(viewToDoIntent)
                }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}