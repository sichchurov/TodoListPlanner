package com.shchurovsi.todolistplanner.data

class GetTodoItemUseCase(private val repository: TodoItemListRepository) {

    fun getTodoItem(todoItemId: Int): TodoItem {
        return repository.getTodoItem(todoItemId)
    }
}