package com.shchurovsi.todolistplanner.data

interface TodoItemListRepository {

    fun addTodoItem(todoItem: TodoItem)

    fun deleteTodoItem(todoItem: TodoItem)

    fun editTodoItem(todoItem: TodoItem)

    fun getTodoItem(todoItemId: Int): TodoItem

    fun getTodoList(): List<TodoItem>
}