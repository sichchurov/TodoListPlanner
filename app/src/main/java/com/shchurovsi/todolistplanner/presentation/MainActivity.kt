package com.shchurovsi.todolistplanner.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
            todoListAdapter.submitList(it)
        }

        todoListAdapter.onTodoClickListener = {
            Log.d("MainActivity", "Click element: $it")
        }

    }

    private fun setupRecyclerView() {
        binding.rcMain.apply {
            todoListAdapter = TodoListAdapter()
            adapter = todoListAdapter
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_COMPLETED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_UNCOMPLETED, MAX_POOL_SIZE)
        }

        setupSwipeListener()

        setupChangingStatusListener()

    }

    private fun setupSwipeListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todoItem = todoListAdapter.currentList[position]
                viewModel.deleteTodoItem(todoItem)
            }
        }).attachToRecyclerView(binding.rcMain)
    }

    private fun setupChangingStatusListener() {
        todoListAdapter.onTodoStatusClickListener = {
            if (it.unCompleted) {
                viewModel.setStatusCompleted(it)
            } else {
                viewModel.setStatusUnCompleted(it)
            }
        }
    }
}