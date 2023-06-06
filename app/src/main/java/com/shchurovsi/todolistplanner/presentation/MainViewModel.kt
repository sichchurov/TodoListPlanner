package com.shchurovsi.todolistplanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shchurovsi.todolistplanner.data.TodoItemRepositoryImpl
import com.shchurovsi.todolistplanner.domain.DeleteTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.EditTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.GetTodoListUseCase
import com.shchurovsi.todolistplanner.domain.TodoItem

class MainViewModel : ViewModel() {

    private val repository = TodoItemRepositoryImpl

    private val getTodoList = GetTodoListUseCase(repository)
    private val editTodoItem = EditTodoItemUseCase(repository)
    private val deleteTodoItem = DeleteTodoItemUseCase(repository)

    private var _todoList = getTodoList.getTodoList()
    val todoList: LiveData<List<TodoItem>>
        get() = _todoList


    fun deleteTodoItem(todoItem: TodoItem) {
        deleteTodoItem.deleteTodoItem(todoItem)
    }

    fun editTodoItem(todoItem: TodoItem) {
        val newTodoItem = todoItem.copy(title = "edited")
        editTodoItem.editTodoItem(newTodoItem)

    }
}