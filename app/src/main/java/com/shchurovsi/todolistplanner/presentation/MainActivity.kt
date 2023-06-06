package com.shchurovsi.todolistplanner.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.todolistplanner.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var counter = 1

        viewModel.todoList.observe(this) {
            Log.d("TAG", "$it")
            if (counter == 1) {
                counter++
                val item = it[1]
                viewModel.deleteTodoItem(item)

            }
        }
    }
}