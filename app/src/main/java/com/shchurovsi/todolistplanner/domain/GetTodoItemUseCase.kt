package com.shchurovsi.todolistplanner.domain

class GetTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun getTodoItem(todoItemId: Int): TodoItem? {
        return repository.getTodoItem(todoItemId)
    }
}