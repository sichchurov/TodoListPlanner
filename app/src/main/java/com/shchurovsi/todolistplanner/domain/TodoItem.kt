package com.shchurovsi.todolistplanner.domain

data class TodoItem(
    val title: String,
    val description: String,
    val unCompleted: Boolean,
    val dateEnd: String = "01-01-2023",
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
