package com.shchurovsi.todolistplanner.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.todolistplanner.R
import com.shchurovsi.todolistplanner.databinding.ActivityTodoItemBinding
import com.shchurovsi.todolistplanner.domain.TodoItem.Companion.UNDEFINED_ID
import com.shchurovsi.todolistplanner.presentation.fragments.TodoItemFragment

class TodoItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoItemBinding

    private var screenMode = UNDEFINED_EXTRA_VALUE
    private var todoItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseExtra()

        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> {
                TodoItemFragment.newInstanceAddTodoItemFragment()
            }

            MODE_EDIT -> {
                TodoItemFragment.newInstanceEditTodoItemFragment(todoItemId)
            }

            else -> {
                throw RuntimeException("Screen mode $screenMode doesn't exists")
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.todo_item_container, fragment)
            .commit()
    }

    private fun parseExtra() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Screen mode doesn't exists")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Screen mode $mode doesn't exists")
        }
        screenMode = mode

        val receivedId = intent.getIntExtra(EXTRA_TODO_ITEM_ID, UNDEFINED_ID)
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_TODO_ITEM_ID)) {
                throw RuntimeException("Param item id doesn't exists")
            }

            todoItemId = receivedId
        }

    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_TODO_ITEM_ID = "extra_todo_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNDEFINED_EXTRA_VALUE = ""

        fun newIntentAddTodo(context: Context): Intent {
            val intent = Intent(context, TodoItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditTodo(context: Context, todoItemId: Int): Intent {
            val intent = Intent(context, TodoItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_TODO_ITEM_ID, todoItemId)
            return intent
        }
    }
}

