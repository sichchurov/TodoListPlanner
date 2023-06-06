package com.shchurovsi.todolistplanner.data

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shchurovsi.todolistplanner.domain.TodoItem
import com.shchurovsi.todolistplanner.domain.TodoItemListRepository

object TodoItemRepositoryImpl : TodoItemListRepository {

    private val todoItemList = mutableListOf<TodoItem>()

    private val mutableLiveData = MutableLiveData<List<TodoItem>>()

    private var counterIncrement = 0

    init {
        val today = Calendar.getInstance().time
        for (i in 'a'..'f') {
            addTodoItem(TodoItem(i.toString(), i.toString().uppercase(), today.toString()))
        }
    }

    override fun addTodoItem(todoItem: TodoItem) {
        if (todoItem.id == TodoItem.UNDEFINED_ID) {
            todoItem.id = counterIncrement++
        }
        todoItemList.add(todoItem)
        updateList()
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoItemList.remove(todoItem)
        updateList()
    }

    override fun editTodoItem(todoItem: TodoItem) {
        val odlElement = getTodoItem(todoItem.id)
        todoItemList.remove(odlElement)
        addTodoItem(todoItem)
    }

    override fun getTodoItem(todoItemId: Int): TodoItem? {
        return todoItemList.find {
            it.id == todoItemId
        }
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return mutableLiveData
    }

    private fun updateList() {
        mutableLiveData.value = todoItemList.toList()
    }
}