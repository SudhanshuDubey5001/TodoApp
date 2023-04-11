package com.sudhanshu.todoapp.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhanshu.todoapp.data.Todo
import com.sudhanshu.todoapp.data.TodoRepository
import com.sudhanshu.todoapp.ui.todo_list.TodoListViewModel
import com.sudhanshu.todoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //now this is the todos creating or editing screen. Here we will either have a todos object already (in case of editing)
    //or we will have a entirely new todos creation

    //so we will create a mutableState of all the variables so they are aware of changes.
    var todo by mutableStateOf<Todo?>(null) // ? because it could new Todos entirely, or edit one
        private set //we do this so only viewmodel can edit it and outside can only see

    var title by mutableStateOf("") //empty title
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    fun get_uiEvent(): SharedFlow<UiEvent> {
        return _uiEvent
    }

    init {
        //first we will see if the todos is already available in case of editing
        val todoId = savedStateHandle.get<Int>("todoID")!!
        //so we will pass -1 if the id is not found
        if (todoId != -1) {
            //now we will first load the old todos witn us------->
            viewModelScope.launch {
                todoRepository.getTodoById(todoId)?.let {
                    title = it.title
                    description = it.description ?: ""
                    todo = it
                    //got the old todos setup
                }
            }
        }
    }

    //now same thing as we did in todoslistevents
    //we will create a sealed data class to see what can we do in this screen
    fun onEvent(event: AddEditTodoEvents) {
        when (event) {
            is AddEditTodoEvents.onAddTitle -> {
                title = event.title
            }
            is AddEditTodoEvents.onAddDescription -> {
                description = event.description
            }
            AddEditTodoEvents.onSaveTodoCLick -> {
                viewModelScope.launch {
                    if (title.isNotBlank()) {
                        val todo = Todo(
                            title,
                            description,
                            todo?.isDone ?: false,   //see if old todos has value otherwise false
                            id = todo?.id
                        )
                        todoRepository.insertTodo(todo)
                    } else {
                        viewModelScope.launch {
                            UiEvent.snackbarShow(
                                content = "Cannot leave the title blank mister/missy/other/doggie/...who knows"
                            )
                        }
                    }
                    _uiEvent.emit(UiEvent.popBackStack)
                }
            }
        }
    }
}