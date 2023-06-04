package com.shchurovsi.todolistplanner.data

class DeleteTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun deleteTodoItem(todoItem: TodoItem) {
        repository.deleteTodoItem(todoItem)
    }
}