package com.shchurovsi.todolistplanner.domain

data class TodoItem(
    val title: String,
    val description: String,
    val dateEnd: String,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
