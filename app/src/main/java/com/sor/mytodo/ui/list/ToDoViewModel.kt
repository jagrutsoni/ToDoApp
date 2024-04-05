package com.sor.mytodo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sor.mytodo.ToDo
import com.sor.mytodo.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _stateStream = MutableStateFlow(ToDoListViewState())
    val stateStream = _stateStream.asStateFlow()

    private var state: ToDoListViewState
        get() = _stateStream.value
        set(newState) {
            _stateStream.update { newState }
        }

    init {
        fetchToDoList()
    }

    fun addToDo(todo: ToDo) {
        viewModelScope.launch {
            repository.addToDoAtFirestore(todo)
            fetchToDoList()
        }
    }

    fun deleteToDo(docID: String) {
        viewModelScope.launch {
            repository.removeToDoFromFirestore(docID)
            fetchToDoList()
        }
    }

    fun updateToDo(docID: String, isChecked:Boolean) {
        val todoList = state.toDoList
        val toDo = todoList.filter { it.id == docID }[0]
        val todoUpdate = toDo.copy(completed = isChecked)
        val todoForUpdate:HashMap<String,Any> = hashMapOf(
            "id" to todoUpdate.id,
            "task" to todoUpdate.task,
            "completed" to todoUpdate.completed,
        )

        viewModelScope.launch {
            repository.updateToDoAtFirestore(docID,todoForUpdate)
            fetchToDoList()
        }
    }

    fun onToDoTextChange(newText: String) {
        state = state.copy(todoText = newText)
    }

    private fun fetchToDoList() {
        viewModelScope.launch {
            state = state.copy(isToDoListLoading = true)
            repository.getToDoListFromFirestore().collectLatest { allToDos ->
                state = state.copy(
                    isToDoListLoading = false,
                    toDoList = allToDos.filter { toDo -> !toDo.completed },
                    toDoListCompleted = allToDos.filter { toDo -> toDo.completed },
                    todoText = ""
                )
            }
        }
    }
}
