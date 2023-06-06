package com.shchurovsi.todolistplanner.domain

import androidx.lifecycle.LiveData

class GetTodoListUseCase(private val repository: TodoItemListRepository) {

    fun getTodoList(): LiveData<List<TodoItem>> {
        return repository.getTodoList()
    }
}