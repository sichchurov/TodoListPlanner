package com.shchurovsi.todolistplanner.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shchurovsi.todolistplanner.R
import com.shchurovsi.todolistplanner.databinding.TodoItemBinding
import com.shchurovsi.todolistplanner.databinding.TodoItemCompletedBinding
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoListAdapter : ListAdapter<TodoItem, RecyclerView.ViewHolder>(TodoItemDiffUtilCallback()) {

    var onTodoStatusClickListener: ((TodoItem) -> Unit)? = null
    var onTodoClickListener: ((TodoItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_UNCOMPLETED -> {
                TodoListViewHolder(
                    TodoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ITEM_VIEW_COMPLETED -> {
                TodoListCompletedViewHolder(
                    TodoItemCompletedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw RuntimeException("The view type value of $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.check_box_filter_panel)

        when (holder) {
            is TodoListViewHolder -> {
                holder.apply {
                    bindTo(getItem(holder.adapterPosition))
                    itemView.setOnClickListener {
                        onTodoClickListener?.invoke(getItem(holder.adapterPosition))
                    }
                }
            }

            is TodoListCompletedViewHolder -> {
                holder.bindTo(getItem(position))
            }
        }

        checkBox.setOnClickListener {
            onTodoStatusClickListener?.invoke(getItem(holder.adapterPosition))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).unCompleted) {
            ITEM_VIEW_COMPLETED
        } else {
            ITEM_VIEW_UNCOMPLETED
        }
    }

    companion object {
        const val ITEM_VIEW_UNCOMPLETED = 0
        const val ITEM_VIEW_COMPLETED = 1

        const val MAX_POOL_SIZE = 15
    }
}