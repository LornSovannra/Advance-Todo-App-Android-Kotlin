package com.frodzy.todo_list.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frodzy.todo_list.models.Todo
import com.frodzy.todo_list.repositories.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository): ViewModel() {
    fun getTodos() = todoRepository.getTodos()

    fun getPendingTasks() = todoRepository.getPendingTasks()

    fun getCompletedTasks() = todoRepository.getCompletedTasks()

    fun upsertTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.upsertTodo(todo)
    }

    fun toggleIsCompleted(id: String, isCompleted: Boolean) = viewModelScope.launch {
        todoRepository.toggleCompleted(id, isCompleted)
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.updateTodo(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.deleteTodo(todo)
    }
}