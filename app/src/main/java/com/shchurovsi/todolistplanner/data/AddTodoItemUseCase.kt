package com.shchurovsi.todolistplanner.data

class AddTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun addTodoItem(todoItem: TodoItem) {
        repository.addTodoItem(todoItem)
    }
}