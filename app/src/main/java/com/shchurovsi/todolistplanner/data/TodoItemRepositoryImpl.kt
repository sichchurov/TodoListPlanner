package com.shchurovsi.todolistplanner.data

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shchurovsi.todolistplanner.domain.TodoItem
import com.shchurovsi.todolistplanner.domain.TodoItemListRepository
import java.util.Date
import kotlin.random.Random

object TodoItemRepositoryImpl : TodoItemListRepository {

//    private val todoItemList = sortedSetOf<TodoItem>({ o1, o2 -> o1.id.compareTo(o2.id)})
    private val todoItemList = mutableListOf<TodoItem>()

    private val mutableLiveData = MutableLiveData<List<TodoItem>>()

    private var counterIncrement = 0

    init {
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm")
        val currentDate = sdf.format(Date())
        val today = Calendar.getInstance()
        for (i in 0..4) {
            addTodoItem(TodoItem(
                "№ $i: Do something!",
                "Common",
                currentDate.toString(),
                Random.nextBoolean()
            ))
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