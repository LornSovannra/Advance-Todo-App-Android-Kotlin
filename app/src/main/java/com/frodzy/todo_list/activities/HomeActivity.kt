package com.frodzy.todo_list.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frodzy.todo_list.R
import com.frodzy.todo_list.adapters.TodoAdapter
import com.frodzy.todo_list.databases.TodoDatabase
import com.frodzy.todo_list.databinding.ActivityHomeBinding
import com.frodzy.todo_list.repositories.TodoRepository
import com.frodzy.todo_list.utils.Separator
import com.frodzy.todo_list.utils.SwipeGesture
import com.frodzy.todo_list.view_models.TodoViewModel
import com.frodzy.todo_list.view_models.TodoViewModelFactory
import java.util.Collections

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var completedTaskAdapter: TodoAdapter
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setUpViewModel()
        setUpEventListeners()
        setUpTodoAdapter()
        setUpCompletedTaskAdapter()
        observeAllTasks()
        observePendingTasks()
        observeCompletedTasks()
    }

    private fun setUpViewModel(){
        val todoRepository = TodoRepository(TodoDatabase(this))
        val todoViewModelProviderFactory = TodoViewModelFactory(todoRepository)
        todoViewModel = ViewModelProvider(this, todoViewModelProviderFactory)[TodoViewModel::class.java]
    }

    private fun setUpSwipeGesture(adapter: TodoAdapter): SwipeGesture {
        val swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(this@HomeActivity)
                            .setTitle("Delete Todo")
                            .setMessage("Are you sure you want to delete it?")
                            .setNegativeButton("Cancel"){ dialog, _ ->
                                dialog.dismiss()
                                adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                            }
                            .setPositiveButton("Delete"){ _, _ ->
                                val position = viewHolder.bindingAdapterPosition
                                val todo = adapter.differ.currentList[position]
                                todoViewModel.deleteTodo(todo)
                                Toast.makeText(this@HomeActivity, "Todo deleted successfully!", Toast.LENGTH_LONG).show()
                            }
                            .setOnCancelListener() {
                                adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                            }
                            .show()
                    }
                }
            }
        }

        return swipeGesture
    }

    private fun setUpTodoAdapter(){
        todoAdapter = TodoAdapter(todoViewModel)
        binding.rvTodoList.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(Separator.VerticalItemSeparator(this@HomeActivity, 16))
            adapter = todoAdapter
        }

        val swipeGesture = setUpSwipeGesture(todoAdapter)
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvTodoList)
    }

    private fun setUpCompletedTaskAdapter(){
        completedTaskAdapter = TodoAdapter(todoViewModel)
        binding.rvCompletedList.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            addItemDecoration(Separator.VerticalItemSeparator(this@HomeActivity, 16))
            adapter = completedTaskAdapter
        }

        val swipeGesture = setUpSwipeGesture(completedTaskAdapter)
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.rvCompletedList)
    }

    private fun setUpEventListeners(){
        binding.apply {
            btnAddToDo.setOnClickListener { navigateToAddToDo() }
        }
    }

    private fun navigateToAddToDo(){
        val addToDoIntent = Intent(this, AddToDoActivity::class.java)
        startActivity(addToDoIntent)
    }

    private fun observeAllTasks(){
        todoViewModel.getTodos().observe(this){ todos ->
            if(todos.isEmpty()){
                binding.apply {
                    todoContainer.visibility = View.GONE
                    emptyContainer.visibility = View.VISIBLE
                }

                return@observe
            }

            binding.apply {
                todoContainer.visibility = View.VISIBLE
                emptyContainer.visibility = View.GONE
            }
        }
    }

    private fun observePendingTasks(){
        todoViewModel.getPendingTasks().observe(this) { pendingTasks ->
            if(pendingTasks.isEmpty()){
                binding.apply {
                    rvTodoList.visibility = View.GONE
                    youHaveCompletedAllTaskContainer.visibility = View.VISIBLE
                }

                return@observe
            }

            binding.apply {
                rvTodoList.visibility = View.VISIBLE
                youHaveCompletedAllTaskContainer.visibility = View.GONE
                todoAdapter.differ.submitList(pendingTasks)
            }
        }
    }

    private fun observeCompletedTasks(){
        todoViewModel.getCompletedTasks().observe(this){ completedTasks ->
            if(completedTasks.isEmpty()){
                binding.apply {
                    completedTaskContainer.visibility = View.GONE
                }

                return@observe
            }

            binding.apply {
                completedTaskContainer.visibility = View.VISIBLE
                completedTaskAdapter.differ.submitList(completedTasks)
            }
        }
    }
}