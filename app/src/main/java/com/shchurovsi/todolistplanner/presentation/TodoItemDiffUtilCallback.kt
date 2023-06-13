package com.shchurovsi.todolistplanner.presentation

import androidx.recyclerview.widget.DiffUtil
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoItemDiffUtilCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}