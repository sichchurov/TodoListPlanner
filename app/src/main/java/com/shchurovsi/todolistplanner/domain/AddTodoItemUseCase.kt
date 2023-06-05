package com.shchurovsi.todolistplanner.domain

class AddTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun addTodoItem(todoItem: TodoItem) {
        repository.addTodoItem(todoItem)
    }
}