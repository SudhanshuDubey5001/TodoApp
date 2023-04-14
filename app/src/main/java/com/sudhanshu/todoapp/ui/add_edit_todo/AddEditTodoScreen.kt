package com.sudhanshu.todoapp.ui.add_edit_todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sudhanshu.todoapp.util.Constants
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

    val focus = LocalFocusManager.current

    Column {
        CenterAlignedTopAppBar(
            title = { Text(text = "Make Todo") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors
                (
                containerColor = Color.Black,
                titleContentColor = Color.White
            )
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    focus.clearFocus()
                },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.onEvent(AddEditTodoEvents.onSaveTodoCLick)
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "save")
                }
            },
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
                        Constants.log("Writing:::" + it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions { focus.clearFocus() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = viewModel.description,
                    placeholder = {
                        Text("Description")
                    },
                    onValueChange = {
                        viewModel.onEvent(AddEditTodoEvents.onAddDescription(it))
                        Constants.log("Writing:::" + it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions { focus.clearFocus() }
                )
            }
        }
    }

}