package com.shchurovsi.todolistplanner.data

class GetTodoListUseCase(private val repository: TodoItemListRepository) {

    fun getTodoList(): List<TodoItem> {
        return repository.getTodoList()
    }
}