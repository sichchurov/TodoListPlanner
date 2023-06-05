package com.shchurovsi.todolistplanner.data

import com.shchurovsi.todolistplanner.domain.TodoItem
import com.shchurovsi.todolistplanner.domain.TodoItemListRepository

object TodoItemRepositoryImpl : TodoItemListRepository {

    private val todoItemList = mutableListOf<TodoItem>()

    private var counterIncrement = 0

    override fun addTodoItem(todoItem: TodoItem) {
        if (counterIncrement == TodoItem.UNDEFINED_ID) {
            todoItem.id = counterIncrement++
        }
        todoItemList.add(todoItem)
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoItemList.remove(todoItem)
    }

    override fun editTodoItem(todoItem: TodoItem) {
        TODO()
    }

    override fun getTodoItem(todoItemId: Int): TodoItem? {
        return todoItemList.find {
            it.id == todoItemId
        }
    }

    override fun getTodoList(): List<TodoItem> {
        return todoItemList.toList()
    }
}