package com.sudhanshu.todoapp.util

//what can happen with the app on the UI layer other than todos stuff(create,delete...) which are only "one" time event--->
// 1. go back if came to the make todos screen (popback)
// 2. navigate to make todos screen
// 3. snackbar show and its action button "undo"

sealed class UiEvent {
    object popBackStack : UiEvent()

    data class navigate(val route: String) : UiEvent()

    data class snackbarShow(
        val content: String,
        val action: String? = null      //if we want to show an action "undo" thats why this is optional
    ) : UiEvent()
}
