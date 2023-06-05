package com.shchurovsi.todolistplanner.domain

class GetTodoListUseCase(private val repository: TodoItemListRepository) {

    fun getTodoList(): List<TodoItem> {
        return repository.getTodoList()
    }
}