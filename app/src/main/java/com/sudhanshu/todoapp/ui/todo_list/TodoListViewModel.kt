package com.sudhanshu.todoapp.ui.todo_list

import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhanshu.todoapp.data.Todo
import com.sudhanshu.todoapp.data.TodoRepository
import com.sudhanshu.todoapp.util.Constants
import com.sudhanshu.todoapp.util.Routes
import com.sudhanshu.todoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// so we created a view model for the screen where all the todos will appear, a list basically
//usually there is one viewModel for each screen

@HiltViewModel //telling hilt, that this is view model class
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository      //here we are asking Hilt, to look into the appModule and find me a method that can PROVIDE me TodoRepository instance
) : ViewModel() {

    //to catch the deleted todos so that if user press undo, we can restore it
    private var deletedTodo: Todo? = null

    //get all the todos
    val todos = todoRepository.getAllTodos()

    //now think about on this screen (list of todos) what can happen UI wise other than Todos App stuff--->
    // now open UiEvent class
    //for one time event we can either use Channel or SharedFlow (both are same except Channel is for single observer)
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    fun get_uiEvent(): SharedFlow<UiEvent>{
        return _uiEvent
    }

    //now thats done, think about what are the UI things for this screen in context of Todos specific operations
    //now make that into another sealed class
    //now we can make a method that checks what kinda UI event is requested by the user using "when"
    fun onTodoEvent(event: TodoListEvents) {
        when (event) {
            is TodoListEvents.OnDoneTodo -> {
                viewModelScope.launch {
                    todoRepository.insertTodo(
                        event.todo.copy(
                            isDone = event.onDone
                        )
                    )
                }
            }
            is TodoListEvents.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    todoRepository.deleteTodo(event.todo)
                    UiEvent.snackbarShow(
                        content = "Todo deleted",
                        action = "Undo"
                    )
                }
            }
            is TodoListEvents.OnTodoClick -> {
                Constants.log("Sending it to edit screen...")
                sendUiEvent(UiEvent.navigate(Routes.TODO_ADD_EDIT + "?todoId=" + event.todo.id))
            }
            TodoListEvents.OnUndoDeleteClick -> {
                viewModelScope.launch {
                    deletedTodo?.let {
                        todoRepository.insertTodo(it)
                    }
                }
            }
            is TodoListEvents.OnAddTodo -> {
                Constants.log("floating button clicked!")
                sendUiEvent(UiEvent.navigate(Routes.TODO_ADD_EDIT))
            }
        }
    }


    //since uiEvent.collectLatest is suspend method, we need to launch it from coroutine, hence make a different method
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}