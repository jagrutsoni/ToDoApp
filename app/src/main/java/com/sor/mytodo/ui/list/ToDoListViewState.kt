package com.sor.mytodo.ui.list

import com.sor.mytodo.ToDo

data class ToDoListViewState(
    val isToDoListLoading: Boolean = false,
    val toDoList: List<ToDo> = emptyList(),
    val toDoListCompleted: List<ToDo> = emptyList(),
    val todoText: String = "",
)
