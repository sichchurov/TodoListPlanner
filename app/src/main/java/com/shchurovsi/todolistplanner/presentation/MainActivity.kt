package com.shchurovsi.todolistplanner.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.shchurovsi.todolistplanner.databinding.ActivityMainBinding
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.ITEM_VIEW_COMPLETED
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.ITEM_VIEW_UNCOMPLETED
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.MAX_POOL_SIZE

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel.todoList.observe(this) {
            todoListAdapter.todoList = it
        }

        binding.checkBoxFilterPanel.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                Log.d("TAG", "Point is checked $isChecked")
            } else {
                Log.d("TAG", "Point is unchecked $isChecked")
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rcMain.apply {
            todoListAdapter = TodoListAdapter()
            adapter = todoListAdapter
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_COMPLETED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_UNCOMPLETED, MAX_POOL_SIZE)
        }
        todoListAdapter.onTodoItemClickListener = {

            viewModel.editTodoItem(it)
        }
    }
}