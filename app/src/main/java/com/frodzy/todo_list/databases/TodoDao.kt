package com.frodzy.todo_list.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.frodzy.todo_list.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY id")
    fun getPendingTasks(): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY id")
    fun getCompletedTasks(): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTodo(toto: Todo)

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun toggleCompleted(id: String, isCompleted: Boolean)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

}