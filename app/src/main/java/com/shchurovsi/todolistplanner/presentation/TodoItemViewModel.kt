package com.shchurovsi.todolistplanner.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shchurovsi.todolistplanner.data.TodoItemListRepositoryImpl
import com.shchurovsi.todolistplanner.domain.AddTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.DeleteTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.EditTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.GetTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoItemViewModel : ViewModel() {

    private val repository = TodoItemListRepositoryImpl

    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)

    private val _inputErrorName = MutableLiveData<Boolean>()
    val inputErrorName: LiveData<Boolean>
        get() = _inputErrorName

    private val _inputErrorDescription = MutableLiveData<Boolean>()
    val inputErrorDescription: LiveData<Boolean>
        get() = _inputErrorName

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    // TODO: add date impl to function
    fun addTodoItem(inputTitle: String?, inputDescription: String?) {
        val title = parseTitle(inputTitle)
        val description = parseInputDescription(inputDescription)
        if (validateFields(title, description)) {
            val todoItem = TodoItem(title, description, true)
            addTodoItemUseCase.addTodoItem(todoItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }

    fun editTodoItem(inputTitle: String?, inputDescription: String?) {
        val title = parseTitle(inputTitle)
        val description = parseInputDescription(inputDescription)
        if (validateFields(title, description)) {
            val todoItem = TodoItem(title, description, true)
            editTodoItemUseCase.editTodoItem(todoItem)
        }
    }

    fun getTodoItem(todoItemId: Int) {
        val item = getTodoItemUseCase.getTodoItem(todoItemId)
        _todoItem.value = item
    }

    private fun parseTitle(inputTitle: String?): String {
        return inputTitle?.trimIndent() ?: ""
    }

    private fun parseInputDescription(inputDescription: String?): String {
        return inputDescription?.trimIndent() ?: ""
    }

    // TODO: add parse date function

    // input validation
    private fun validateFields(title: String, description: String): Boolean {
        var result = true

        if (title.isBlank()) {
            _inputErrorName.value = true
            result = false
        }

        if (description.isBlank()) {
            _inputErrorDescription.value = true
            result = false
        }
        return result
    }

    private fun resetInputTitleError() {
        _inputErrorName.value = false
    }

    private fun resetInputDesciptionError() {
        _inputErrorDescription.value = false
    }

}