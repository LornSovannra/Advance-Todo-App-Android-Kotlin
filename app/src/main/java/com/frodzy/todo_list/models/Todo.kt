package com.frodzy.todo_list.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "todos")
@Parcelize
data class Todo(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val note: String,
    val isCompleted: Boolean,
) : Parcelable

val todoMockUp = listOf<Todo>(
    Todo(UUID.randomUUID().toString(), "Testing 1", "Note testing 1", false),
    Todo(UUID.randomUUID().toString(), "Testing 2", "Note testing 2", false),
    Todo(UUID.randomUUID().toString(), "Testing 3", "Note testing 3", false),
    Todo(UUID.randomUUID().toString(), "Testing 4", "Note testing 4", false),
    Todo(UUID.randomUUID().toString(), "Testing 5", "Note testing 5", false),
    Todo(UUID.randomUUID().toString(), "Testing 6", "Note testing 6", false),
)