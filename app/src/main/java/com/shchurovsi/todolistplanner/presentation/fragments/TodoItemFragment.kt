package com.shchurovsi.todolistplanner.presentation.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.shchurovsi.todolistplanner.R
import com.shchurovsi.todolistplanner.databinding.FragmentTodoItemBinding
import com.shchurovsi.todolistplanner.domain.TodoItem.Companion.UNDEFINED_ID
import com.shchurovsi.todolistplanner.presentation.TodoItemActivity
import com.shchurovsi.todolistplanner.presentation.TodoItemViewModel
import com.shchurovsi.todolistplanner.presentation.TodoItemViewModel.Companion.INPUT_DATE_FORMAT
import java.sql.Date
import java.util.Locale

class TodoItemFragment : Fragment() {

    private val todoItemViewModel: TodoItemViewModel by activityViewModels()

    private var screenMode: String = UNDEFINED_EXTRA_VALUE
    private var todoItemId: Int = UNDEFINED_ID

    private var _binding: FragmentTodoItemBinding? = null
    private val binding: FragmentTodoItemBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startApp()
    }

    private fun startApp() = with(binding) {

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

        btCancelDelete.setOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }

        btDatePicker.setOnClickListener { showDatePickerDialog() }
    }

    private fun launchEditMode() = with(binding) {

        btAddDone.text = getString(R.string.done)
        btCancelDelete.text = getString(R.string.delete)

        todoItemViewModel.getTodoItem(todoItemId)

        todoItemViewModel.todoItem.observe(viewLifecycleOwner) {
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
        todoItemViewModel.todoItem.observe(viewLifecycleOwner) {
            todoItemViewModel.deleteTodoItem(it)
        }

        // TODO: before finish add special screen with information that deleted is finished
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Screen mode doesn't exists")
        }

        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Screen mode $mode doesn't exists")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(TODO_ITEM_ID)) {
                throw RuntimeException("Param item id doesn't exists")
            }

            todoItemId = args.getInt(TODO_ITEM_ID)
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

                val date = Date(cal.time.time)

                val formatter = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                val formattedDate = formatter.format(parser.parse(date.toString()))

                binding.edDateEnd.setText(formattedDate)

            }

        DatePickerDialog(
            requireContext(), R.style.MyDatePickerStyle, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setMessageErrorInputFields() = with(binding) {

        todoItemViewModel.apply {

            inputErrorTitle.observe(viewLifecycleOwner) {
                val message = if (it) {
                    getString(R.string.empty_field_error, "${tvTitleItem.text}")
                } else {
                    null
                }
                tilTitle.error = message
            }

            inputErrorDate.observe(viewLifecycleOwner) {
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
        todoItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
            Log.d("TAG", "PRESSED")
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val TODO_ITEM_ID = "extra_todo_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNDEFINED_EXTRA_VALUE = ""

        fun newInstanceAddTodoItemFragment(): TodoItemFragment {
            return TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditTodoItemFragment(todoItemId: Int): TodoItemFragment {
            return TodoItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(TODO_ITEM_ID, todoItemId)
                }
            }
        }
    }
}