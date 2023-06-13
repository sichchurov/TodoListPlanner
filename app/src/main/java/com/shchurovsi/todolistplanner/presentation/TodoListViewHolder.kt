package com.shchurovsi.todolistplanner.presentation

import androidx.recyclerview.widget.RecyclerView
import com.shchurovsi.todolistplanner.databinding.TodoItemBinding
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoListViewHolder(private val binding: TodoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindTo(todoItem: TodoItem) = with(binding) {
        tvTitle.text = todoItem.title
        tvDate.text = todoItem.dateEnd
        checkBoxFilterPanel.isChecked = false
    }
}