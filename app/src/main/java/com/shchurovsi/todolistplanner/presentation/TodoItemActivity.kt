package com.shchurovsi.todolistplanner.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.todolistplanner.R

class TodoItemActivity : AppCompatActivity() {

    private val viewModel = TodoItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_item)

        val item = viewModel.todoItem.value
    }
}