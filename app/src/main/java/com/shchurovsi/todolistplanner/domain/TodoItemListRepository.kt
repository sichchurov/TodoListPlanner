package com.shchurovsi.todolistplanner.domain

import androidx.lifecycle.LiveData

interface TodoItemListRepository {

    fun addTodoItem(todoItem: TodoItem)

    fun deleteTodoItem(todoItem: TodoItem)

    fun editTodoItem(todoItem: TodoItem)

    // TODO: think about nullable implementation
    fun getTodoItem(todoItemId: Int): TodoItem?

    fun getTodoList(): LiveData<List<TodoItem>>
}