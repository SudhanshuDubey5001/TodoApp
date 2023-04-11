package com.sudhanshu.todoapp.ui.todo_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sudhanshu.todoapp.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {

    //now refer the viewModel class as we are implementing its stuff one by one
    //first is todoslist to get all then list, we collect it as state cuz each time value changes, whole compose
    //rebuilds and therefore we want todos as state
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberBottomSheetScaffoldState()
    //next is _uievent
    LaunchedEffect(key1 = true) {
        viewModel.get_uiEvent().collectLatest { event ->
            when (event) {
                is UiEvent.navigate -> {
                    onNavigate(event)
                }
                UiEvent.popBackStack -> {

                }
                is UiEvent.snackbarShow -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.content,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onTodoEvent(TodoListEvents.OnUndoDeleteClick)
                    }
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onTodoEvent(TodoListEvents.OnAddTodo)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(todos.value) {
                TodoItem(
                    todo = it,
                    //event = method with TodolistEvent as argument hence, we pass onTodoEvent which
                    // is a method. So this is the way we pass a method
                    event = viewModel::onTodoEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onTodoEvent(TodoListEvents.OnTodoClick(it))
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}