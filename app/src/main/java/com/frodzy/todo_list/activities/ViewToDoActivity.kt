package com.frodzy.todo_list.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.frodzy.todo_list.R
import com.frodzy.todo_list.databases.TodoDatabase
import com.frodzy.todo_list.databinding.ActivityViewToDoBinding
import com.frodzy.todo_list.models.Todo
import com.frodzy.todo_list.repositories.TodoRepository
import com.frodzy.todo_list.view_models.TodoViewModel
import com.frodzy.todo_list.view_models.TodoViewModelFactory

class ViewToDoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewToDoBinding
    private lateinit var todoViewModel: TodoViewModel
    private var todo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewToDoBinding.inflate(layoutInflater)
        todo = intent.getParcelableExtra<Todo>("TODO")
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpViewModel()
        initializeData()
        setUpEventListeners()
    }

    private fun setUpViewModel(){
        val todoRepository = TodoRepository(TodoDatabase(this))
        val todoViewModelProviderFactory = TodoViewModelFactory(todoRepository)
        todoViewModel = ViewModelProvider(this, todoViewModelProviderFactory)[TodoViewModel::class.java]
    }

    private fun initializeData(){
        binding.apply {
            tvTitle.setText(todo?.title)
            tvNote.setText(todo?.note)
        }
    }

    private fun setUpEventListeners(){
        binding.apply {
            btnUpdate.setOnClickListener {
                val updatedTodo = Todo(
                    todo?.id.toString(),
                    tvTitle.text.toString(),
                    tvNote.text.toString(),
                    todo?.isCompleted ?: false,
                )

                todoViewModel.updateTodo(updatedTodo)
                finish()
                Toast.makeText(this@ViewToDoActivity, "Todo updated!", Toast.LENGTH_LONG).show()
            }

            btnDone.setOnClickListener {
                todo?.let { it1 -> todoViewModel.toggleIsCompleted(it1.id, true) }
                finish()
                Toast.makeText(this@ViewToDoActivity, "Congratulation!", Toast.LENGTH_LONG).show()
            }

            btnDelete.setOnClickListener {
                AlertDialog.Builder(this@ViewToDoActivity)
                    .setTitle("Delete Todo")
                    .setMessage("Are you sure you want to delete it?")
                    .setNegativeButton("Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Delete"){ _, _ ->
                        todoViewModel.deleteTodo(todo!!)
                        finish()
                        Toast.makeText(this@ViewToDoActivity, "Todo deleted successfully!", Toast.LENGTH_LONG).show()
                    }
                    .show()
            }
        }
    }
}