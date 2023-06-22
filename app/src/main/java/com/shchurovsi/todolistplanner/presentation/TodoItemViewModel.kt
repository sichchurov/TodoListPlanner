package com.shchurovsi.todolistplanner.presentation

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shchurovsi.todolistplanner.data.TodoItemListRepositoryImpl
import com.shchurovsi.todolistplanner.domain.AddTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.DeleteTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.EditTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.GetTodoItemUseCase
import com.shchurovsi.todolistplanner.domain.TodoItem
import java.text.ParseException
import java.util.Locale

class TodoItemViewModel : ViewModel() {

    private val repository = TodoItemListRepositoryImpl

    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)

    private val _inputErrorTitle = MutableLiveData<Boolean>()
    val inputErrorTitle: LiveData<Boolean>
        get() = _inputErrorTitle

    private val _inputErrorDate = MutableLiveData<Boolean>()
    val inputErrorDate: LiveData<Boolean>
        get() = _inputErrorDate

    // если все поля заполнены, то переходим на главный экран
    // проверка делается в случае добавления объекта из другого потока
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    // TODO: add date impl to function
    fun addTodoItem(inputTitle: String?, inputDescription: String?, inputDateEnd: String?) {
        val title = parseTitle(inputTitle)
        val description = parseInputDescription(inputDescription)
        val date = parseInputDate(inputDateEnd)

        if (validateFields(title, date)) {
            val todoItem = TodoItem(title, description, false, date)
            addTodoItemUseCase.addTodoItem(todoItem)
            finishWork()
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }

    fun editTodoItem(inputTitle: String?, inputDescription: String?, inputDateEnd: String?) {
        val title = parseTitle(inputTitle)
        val description = parseInputDescription(inputDescription)
        val date = parseInputDate(inputDateEnd)

        if (validateFields(title, date)) {
            _todoItem.value?.let {
                val item = it.copy(title = title, description = description, dateEnd = date)
                editTodoItemUseCase.editTodoItem(item)
                finishWork()
            }
        }
    }

    fun getTodoItem(todoItemId: Int) {
        val item = getTodoItemUseCase.getTodoItem(todoItemId)
        _todoItem.value = item
    }

    private fun parseTitle(inputTitle: String?): String {
        return inputTitle?.trim() ?: ""
    }

    private fun parseInputDescription(inputDescription: String?): String {
        return inputDescription?.trim() ?: ""
    }

    // TODO: поискать лучшее решение и поправить регулярку
    private fun parseInputDate(inputDate: String?): String {
        val parser = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        val formatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())

        inputDate?.trim()?.let {
            try {
                if (it.matches(Regex("""[0-9]{2}.[0-9]{2}.20[2-9]{2}""")) || it.isEmpty()) {
                    return formatter.format(parser.parse(it))
                } else return INCORRECT_DATE
            } catch (_: ParseException) {
                return ""
            }
        }
        return INCORRECT_DATE
    }

    private fun validateFields(title: String, dateEnd: String): Boolean {
        var result = true

        if (title.isBlank()) {
            _inputErrorTitle.value = true
            result = false
        }

        if (dateEnd == INCORRECT_DATE) {
            _inputErrorDate.value = true
            result = false
        }
        return result
    }

    fun resetInputTitleError() {
        _inputErrorTitle.value = false
    }

    fun resetInputDateError() {
        _inputErrorDate.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    companion object {
        private const val INCORRECT_DATE = "incorrect date"
        const val INPUT_DATE_FORMAT = "dd.MM.yyyy"
    }

}