package com.frodzy.todo_list.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frodzy.todo_list.repositories.TodoRepository

class TodoViewModelFactory(private val todoRepository: TodoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(todoRepository) as T
    }
}