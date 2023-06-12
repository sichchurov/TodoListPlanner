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
        viewModel.todoList.observe(this) { todoList ->
            todoListAdapter.todoList = todoList
        }

        swipeToRemoveTodoItem()

        changeTodoStatus()

    }

    private fun swipeToRemoveTodoItem() {
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition
                    val todoItems = viewModel.todoList.value
                    if (!todoItems.isNullOrEmpty()) {
                        viewModel.deleteTodoItem(todoItems[position])
                    }
                }
            }
        ).attachToRecyclerView(binding.rcMain)
    }

    private fun setupRecyclerView() {
        binding.rcMain.apply {
            todoListAdapter = TodoListAdapter()
            adapter = todoListAdapter
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_COMPLETED, MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ITEM_VIEW_UNCOMPLETED, MAX_POOL_SIZE)
        }
        todoListAdapter.onTodoStatusClickListener = {
            Log.d("MainActivity", "Click element: ${it.id}")

        }
    }

    private fun changeTodoStatus() {
        todoListAdapter.onTodoStatusClickListener = {
            if (it.unCompleted) {
                viewModel.setStatusCompleted(it)
            } else {
                viewModel.setStatusUnCompleted(it)
            }
        }

    }
}