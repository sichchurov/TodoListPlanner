package com.shchurovsi.todolistplanner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shchurovsi.todolistplanner.domain.TodoItem
import com.shchurovsi.todolistplanner.domain.TodoItemListRepository
import kotlin.random.Random

object TodoItemListRepositoryImpl : TodoItemListRepository {

    private val todoItemList = sortedSetOf<TodoItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private val mutableLiveData = MutableLiveData<List<TodoItem>>()

    private var counterIncrement = 0

    init {
        for (i in 0..5) {
            addTodoItem(
                TodoItem(
                    "№ $i: Do something!",
                    "Common",
                    Random.nextBoolean()
                )
            )
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

    override fun getTodoItem(todoItemId: Int): TodoItem {
        return todoItemList.find {
            it.id == todoItemId
        } ?: throw RuntimeException("Element with id $todoItemId not found")
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return mutableLiveData
    }

    private fun updateList() {
        mutableLiveData.value = todoItemList.toList()
    }
}