package com.shchurovsi.todolistplanner.presentation

import android.accounts.AuthenticatorDescription
import androidx.lifecycle.LiveData
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

    fun editTodoItem(todoItem: TodoItem) {
        val title = parseTitle(todoItem.title)
        val description = parseInputDescription(todoItem.description)
        if (validateFields(title, description)) {
            editTodoItemUseCase.editTodoItem(todoItem)
        }
    }

    fun getTodoItem(todoItemId: Int) {
        val item = getTodoItemUseCase.getTodoItem(todoItemId)
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
            // TODO: show error input name
            result = false
        }

        if (description.isBlank()) {
            // TODO: show error input description
            result = false
        }
        return result
    }

}