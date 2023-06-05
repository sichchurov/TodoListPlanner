package com.shchurovsi.todolistplanner.domain

class EditTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun editTodoItem(todoItem: TodoItem) {
        repository.editTodoItem(todoItem)
    }
}