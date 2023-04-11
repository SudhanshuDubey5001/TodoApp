package com.sudhanshu.todoapp.ui.add_edit_todo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sudhanshu.todoapp.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    popbackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.get_uiEvent().collectLatest { event ->
            when (event) {
                UiEvent.popBackStack -> {
                    popbackStack()
                }
                is UiEvent.snackbarShow -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.content,
                        actionLabel = event.action
                    )
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = viewModel.title,
                placeholder = {
                    Text("Title")
                },
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvents.onAddTitle(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = viewModel.description,
                placeholder = {
                    Text("Description")
                },
                onValueChange = {
                    viewModel.onEvent(AddEditTodoEvents.onAddDescription(it))
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                singleLine = false
            )

        }
    }

}