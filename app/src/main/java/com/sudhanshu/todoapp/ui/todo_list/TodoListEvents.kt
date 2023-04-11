package com.sudhanshu.todoapp.ui.todo_list

import com.sudhanshu.todoapp.data.Todo

//these are the Todos specific stuff that can happen in TodoList activity
sealed class TodoListEvents {
    //add a todos using the button
    object OnAddTodo : TodoListEvents()

    //todos is done or not (checked or not checked)
    data class OnDoneTodo(val todo: Todo, val onDone: Boolean): TodoListEvents()

    //todos undo button on snackbar
    object OnUndoDeleteClick: TodoListEvents()

    //on clicking the todos already in the list
    data class OnTodoClick(val todo: Todo): TodoListEvents()

    //on clicking the delete button on any todos
    data class OnDeleteTodoClick(val todo: Todo): TodoListEvents()
}
