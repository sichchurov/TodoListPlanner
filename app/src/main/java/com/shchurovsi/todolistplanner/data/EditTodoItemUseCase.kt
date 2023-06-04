package com.shchurovsi.todolistplanner.data

class EditTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun editTodoItem(todoItem: TodoItem) {
        repository.editTodoItem(todoItem)
    }
}