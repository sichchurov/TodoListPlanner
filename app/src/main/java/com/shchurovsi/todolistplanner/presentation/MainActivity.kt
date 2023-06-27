package com.shchurovsi.todolistplanner.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shchurovsi.todolistplanner.R
import com.shchurovsi.todolistplanner.databinding.ActivityMainBinding
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.ITEM_VIEW_COMPLETED
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.ITEM_VIEW_UNCOMPLETED
import com.shchurovsi.todolistplanner.presentation.TodoListAdapter.Companion.MAX_POOL_SIZE
import com.shchurovsi.todolistplanner.presentation.fragments.TodoItemFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var todoItemContainer: FragmentContainerView? = null

    private val viewModel: MainViewModel by viewModels()

    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.todoList.observe(this) {
            todoListAdapter.submitList(it)
        }

        setupRecyclerView()

        setupRecyclerViewHeader()

        todoItemContainer = findViewById(R.id.fragment_container_view)
        

        todoListAdapter.onTodoClickListener = {
            if (isOnePainView()) {
                startActivity(TodoItemActivity.newIntentEditTodo(this@MainActivity, it.id))
            } else {
                setupLandViewTodoAddEditItem(
                    TodoItemFragment.newInstanceEditTodoItemFragment(it.id)
                )
            }
        }

        binding.fab.setOnClickListener {
            if (isOnePainView()) {
                startActivity(TodoItemActivity.newIntentAddTodo(this))
            } else {
                setupLandViewTodoAddEditItem(
                    TodoItemFragment.newInstanceAddTodoItemFragment()
                )
            }
        }
    }

    private fun isOnePainView(): Boolean {
        return todoItemContainer == null
    }

    private fun setupLandViewTodoAddEditItem(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .addToBackStack(null)
            .commit()

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

    private fun setupRecyclerViewHeader() = with(binding) {
        viewModel.apply {

            tvTodosCount.text = getString(
                R.string.todo_count,
                todoList.value?.size ?: 0
            )

            todoList.observe(this@MainActivity) {
                tvTodosCount.text =
                    getString(R.string.todo_count, it.size)
            }

        }

        // TODO: implement view all button
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