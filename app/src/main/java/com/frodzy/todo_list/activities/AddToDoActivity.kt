package com.frodzy.todo_list.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.frodzy.todo_list.R
import com.frodzy.todo_list.databases.TodoDatabase
import com.frodzy.todo_list.databinding.ActivityAddToDoBinding
import com.frodzy.todo_list.models.Todo
import com.frodzy.todo_list.repositories.TodoRepository
import com.frodzy.todo_list.view_models.TodoViewModel
import com.frodzy.todo_list.view_models.TodoViewModelFactory
import java.util.UUID

class AddToDoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddToDoBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpViewModel()
        setUpEventListeners()
    }

    private fun setUpViewModel(){
        val todoRepository = TodoRepository(TodoDatabase(this))
        val todoViewModelProviderFactory = TodoViewModelFactory(todoRepository)
        todoViewModel = ViewModelProvider(this, todoViewModelProviderFactory)[TodoViewModel::class.java]
    }

    private fun setUpEventListeners(){
        binding.apply {
            btnSave.setOnClickListener {
                val title = edTitle.text
                val description = edDescription.text

                val todo = Todo(
                    UUID.randomUUID().toString(),
                    title.toString(),
                    description.toString(),
                    false,
                )

                todoViewModel.upsertTodo(todo)
                finish()
            }
        }
    }
}