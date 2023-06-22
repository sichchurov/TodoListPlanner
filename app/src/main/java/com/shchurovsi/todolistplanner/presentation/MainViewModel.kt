package com.shchurovsi.todolistplanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shchurovsi.todolistplanner.data.TodoItemListRepositoryImpl
import com.shchurovsi.todolistplanner.domain.DeleteTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.EditTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.GetTodoListUseCase
import com.shchurovsi.todolistplanner.domain.TodoItem

class MainViewModel : ViewModel() {

    private val repository = TodoItemListRepositoryImpl

    private val getTodoList = GetTodoListUseCase(repository)
    private val editTodoItem = EditTodoItemUseCase(repository)
    private val deleteTodoItem = DeleteTodoItemUseCase(repository)

    private var _todoList = getTodoList.getTodoList()
    val todoList: LiveData<List<TodoItem>>
        get() = _todoList

    fun deleteTodoItem(todoItem: TodoItem) {
        deleteTodoItem.deleteTodoItem(todoItem)
    }

    fun setStatusCompleted(todoItem: TodoItem) {
        val newTodoItem = todoItem.copy(unCompleted = false)
        editTodoItem.editTodoItem(newTodoItem)
    }

    fun setStatusUnCompleted(todoItem: TodoItem) {
        val newTodoItem = todoItem.copy(unCompleted = true)
        editTodoItem.editTodoItem(newTodoItem)
    }
}