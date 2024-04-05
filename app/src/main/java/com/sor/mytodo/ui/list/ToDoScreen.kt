package com.sor.mytodo.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sor.mytodo.ToDo
import java.util.UUID

@Composable
@ExperimentalMaterial3Api
fun ToDoRoute(
    viewModel: ToDoViewModel = hiltViewModel()
) {
    val viewState: ToDoListViewState by viewModel.stateStream.collectAsState()
    ToDoScreen(
        viewState = viewState,
        viewModel = viewModel
    )
}

@Composable
@ExperimentalMaterial3Api
fun ToDoScreen(
    viewState: ToDoListViewState,
    viewModel: ToDoViewModel = hiltViewModel()
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Title()

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize(), contentAlignment = Alignment.Center) {
            if (!viewState.isToDoListLoading) {
                if (viewState.toDoList.isEmpty()) {
                    EmptyLabel()
                } else {
                    Column {
                        ToDoList(viewModel, toDoList = viewState.toDoList)
                        Divider()
                        ToDoList(viewModel, toDoList = viewState.toDoListCompleted)
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }

        AddToDo(viewModel, viewState = viewState)
    }
}

@Composable
fun Title() {
    Text(
        modifier = Modifier.padding(16.dp),
        text = "My To-Do",
        fontSize = 36.sp,
    )
}

@Composable
fun EmptyLabel() {
    Text("Nothing here yet.\nPlease add your first TO-DO",)
}

@Composable
fun ToDoList(viewModel: ToDoViewModel, toDoList: List<ToDo>) {
    LazyColumn(modifier = Modifier.wrapContentHeight()) {
        items(key = {it.id }, items = toDoList) { item ->

            val checkedState = remember { mutableStateOf(item.completed) }

            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 8.dp, 16.dp, 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFCFCDD3)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.task,modifier = Modifier
                    .padding(16.dp, 8.dp, 16.dp, 8.dp)
                    .wrapContentHeight(), fontSize = 24.sp, color = Color.Black)
                Spacer(Modifier.weight(1f))
                Checkbox(modifier = Modifier.padding(end = 8.dp), checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it
                        viewModel.updateToDo(item.id,it)
                    }
                )
                TextButton(onClick = { viewModel.deleteToDo(item.id)  })
                {
                    Icon(Icons.Default.Clear,"")
                }

            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDo(viewModel: ToDoViewModel, viewState: ToDoListViewState) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(16.dp, 8.dp, 16.dp, 8.dp)
        .clip(RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically){

        TextField(modifier = Modifier
            .weight(1f)
            .padding(8.dp), value = viewState.todoText, placeholder = { Text("Enter To-Do") },
            onValueChange = viewModel::onToDoTextChange
        )
        Button(modifier = Modifier.padding(8.dp), onClick = {
            viewModel.addToDo(ToDo(UUID.randomUUID().toString(), viewState.todoText))
        }) {
            Text(text = "ADD")
        }
    }
}
