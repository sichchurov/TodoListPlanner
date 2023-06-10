package com.shchurovsi.todolistplanner.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shchurovsi.todolistplanner.databinding.TodoItemBinding
import com.shchurovsi.todolistplanner.databinding.TodoItemCompletedBinding
import com.shchurovsi.todolistplanner.domain.TodoItem

class TodoListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null

    var todoList = listOf<TodoItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var count = 0
    private var count2 = 0

    private inner class TodoListViewHolder(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(todoItem: TodoItem) = with(binding) {
            tvTitle.text = todoItem.title
            tvDate.text = todoItem.dateEnd
            checkBoxFilterPanel.isChecked = false
            root.setOnClickListener {
                onTodoItemClickListener?.invoke(todoItem)
            }
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    android.R.color.holo_orange_light
                )
            )
        }
    }

    private inner class TodoListCompletedViewHolder(private val binding: TodoItemCompletedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(todoItem: TodoItem) = with(binding) {
            tvTitle.text = todoItem.title
            tvDate.text = todoItem.dateEnd
            checkBoxFilterPanel.isChecked = true
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    android.R.color.holo_green_dark
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("TodoListAdapter", "TodoListViewHolder count: ${++count}")
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
                Log.d("TodoListAdapter", "TodoListCompletedViewHolder count: ${++count2}")
                TodoListCompletedViewHolder(
                    TodoItemCompletedBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw RuntimeException("The viewtype value of $viewType is not supported")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoListViewHolder -> {
                holder.bindTo(todoList[position])
            }

            is TodoListCompletedViewHolder -> {
                holder.bindTo(todoList[position])
            }
        }
    }

    interface TodoItemClickListener {
        fun todoItemClick(todoItem: TodoItem)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (todoList[position].completed) {
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