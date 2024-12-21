package com.frodzy.todo_list.repositories

import com.frodzy.todo_list.databases.TodoDatabase
import com.frodzy.todo_list.models.Todo

class TodoRepository(db: TodoDatabase) {
    private val todoDao = db.getTodoDao()

    fun getTodos() = todoDao.getTodos()
    fun getPendingTasks() = todoDao.getPendingTasks()
    fun getCompletedTasks() = todoDao.getCompletedTasks()
    suspend fun upsertTodo(todo: Todo) = todoDao.upsertTodo(todo)
    suspend fun toggleCompleted(id: String, isCompleted: Boolean) = todoDao.toggleCompleted(id, isCompleted)
    suspend fun updateTodo(todo: Todo) = todoDao.updateTodo(todo)
    suspend fun deleteTodo(todo: Todo) = todoDao.deleteTodo(todo)
}