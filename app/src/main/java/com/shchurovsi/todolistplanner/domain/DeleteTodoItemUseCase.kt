package com.shchurovsi.todolistplanner.domain

class DeleteTodoItemUseCase(private val repository: TodoItemListRepository) {
    fun deleteTodoItem(todoItem: TodoItem) {
        repository.deleteTodoItem(todoItem)
    }
}