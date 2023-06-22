package com.shchurovsi.todolistplanner.presentation

import androidx.recyclerview.widget.RecyclerView
import com.shchurovsi.todolistplanner.databinding.TodoItemCompletedBinding
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoListCompletedViewHolder(private val binding: TodoItemCompletedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(todoItem: TodoItem) = with(binding) {
        tvTitle.text = todoItem.title
        tvDate.text = todoItem.dateEnd
        checkBoxFilterPanel.isChecked = true
    }
}