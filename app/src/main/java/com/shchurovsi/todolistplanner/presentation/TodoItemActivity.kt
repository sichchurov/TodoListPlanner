package com.shchurovsi.todolistplanner.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shchurovsi.todolistplanner.R
import com.shchurovsi.todolistplanner.databinding.ActivityTodoItemBinding
import com.shchurovsi.todolistplanner.domain.TodoItem.Companion.UNDEFINED_ID
import com.shchurovsi.todolistplanner.presentation.TodoItemViewModel.Companion.INPUT_DATE_FORMAT
import java.sql.Date
import java.util.Locale

class TodoItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoItemBinding

    private val todoItemViewModel: TodoItemViewModel by viewModels()

    private var screenMode = UNDEFINED_EXTRA_VALUE
    private var todoItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startApp()
    }

    private fun startApp() = with(binding) {
        parseExtra()

        launchRightMode()

        setMessageErrorInputFields()

        addTextChangerListeners()

        getFinishWorkStatus()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> {
                launchAddMode()
            }

            MODE_EDIT -> {
                launchEditMode()
            }
        }
    }

    private fun launchAddMode() = with(binding) {

        btAddDone.setOnClickListener {
            todoItemViewModel.addTodoItem(
                edTitle.text?.toString(),
                edDescription.text?.toString(),
                edDateEnd.text.toString()
            )
        }

        btCancelDelete.setOnClickListener { finish() }

        btDatePicker.setOnClickListener { showDatePickerDialog() }
    }

    private fun launchEditMode() = with(binding) {

        todoItemViewModel.getTodoItem(todoItemId)

        todoItemViewModel.todoItem.observe(this@TodoItemActivity) {
            edTitle.setText(it.title)
            edDescription.setText(it.description)
            edDateEnd.setText(it.dateEnd)
        }

        btAddDone.setOnClickListener {
            todoItemViewModel.editTodoItem(
                edTitle.text?.toString(),
                edDescription.text?.toString(),
                edDateEnd.text?.toString()
            )
        }

        btDatePicker.setOnClickListener { showDatePickerDialog() }

        btCancelDelete.setOnClickListener {
            deleteTodoItem()
        }
    }

    private fun deleteTodoItem() = with(binding) {
        todoItemViewModel.todoItem.observe(this@TodoItemActivity) {
            todoItemViewModel.deleteTodoItem(it)
        }

        // TODO: before finish add special screen with information that deleted is finished
        finish()
    }

    private fun parseExtra() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Screen mode doesn't exists")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Screen mode $mode doesn't exists")
        }
        screenMode = mode

        val receivedId = intent.getIntExtra(EXTRA_TODO_ITEM_ID, UNDEFINED_ID)
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_TODO_ITEM_ID)) {
                throw RuntimeException("Param item id doesn't exists")
            }
            binding.apply {
                btAddDone.text = getString(R.string.done)
                btCancelDelete.text = getString(R.string.delete)
            }

            todoItemId = receivedId
        }

    }

    // TODO: поискать лучшее решение
    private fun showDatePickerDialog() {

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val date = Date(cal.time.time) // 2023-06-22

                val formatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
                val parser =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val formattedDate = formatter.format(parser.parse(date.toString()))

                binding.edDateEnd.setText(formattedDate)

            }

        DatePickerDialog(
            this, R.style.MyDatePickerStyle, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setMessageErrorInputFields() = with(binding) {

        todoItemViewModel.apply {

            inputErrorTitle.observe(this@TodoItemActivity) {
                val message = if (it) {
                    getString(R.string.empty_field_error, "${tvTitleItem.text}")
                } else {
                    null
                }
                tilTitle.error = message
            }

            inputErrorDate.observe(this@TodoItemActivity) {
                val message = if (it) {
                    btDatePicker.visibility = View.GONE
                    getString(R.string.incorrect_date)
                } else {
                    null
                }
                tilDateEnd.error = message
            }
        }

    }

    private fun addTextChangerListeners() = with(binding) {

        edTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                todoItemViewModel.resetInputTitleError()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edDateEnd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                todoItemViewModel.resetInputDateError()
                btDatePicker.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun getFinishWorkStatus() {
        todoItemViewModel.shouldCloseScreen.observe(this@TodoItemActivity) {
            finish()
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_TODO_ITEM_ID = "extra_todo_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNDEFINED_EXTRA_VALUE = ""

        const val REQUIRED_FORMAT_DATE = "d MMM yyyy"

        fun newIntentAddTodo(context: Context): Intent {
            val intent = Intent(context, TodoItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditTodo(context: Context, todoItemId: Int): Intent {
            val intent = Intent(context, TodoItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_TODO_ITEM_ID, todoItemId)
            return intent
        }
    }
}

