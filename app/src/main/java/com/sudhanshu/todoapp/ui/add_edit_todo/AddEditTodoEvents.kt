package com.sudhanshu.todoapp.ui.add_edit_todo

//see what can we do on this screen
sealed class AddEditTodoEvents {
//    add title
    data class onAddTitle(val title: String) : AddEditTodoEvents()

    //add description
    data class onAddDescription(val description: String) : AddEditTodoEvents()

    //click onsavetodo button
    object onSaveTodoCLick : AddEditTodoEvents()
}
