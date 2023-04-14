package com.sudhanshu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sudhanshu.todoapp.ui.add_edit_todo.AddEditTodoScreen
import com.sudhanshu.todoapp.ui.theme.TODOAppTheme
import com.sudhanshu.todoapp.ui.todo_list.TodoListScreen
import com.sudhanshu.todoapp.util.Constants
import com.sudhanshu.todoapp.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  //we tell the dagger entry point of app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOAppTheme {
                //see how the navigation will work --->
                val navController = rememberNavController()
                NavHost(
                    navController = navController,  //pass the navCOntroller
                    startDestination = Routes.TODO_LIST //tell the starting screen
                ) {
                    composable(Routes.TODO_LIST) {
                        TodoListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                    composable(
                        route = Routes.TODO_ADD_EDIT + "?todoId={${Constants.TODO_ID}}", //todos id passed will replced in {todoId}
                        arguments = listOf(
                            navArgument(name = Constants.TODO_ID){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ){
                        AddEditTodoScreen(
                            popbackStack = { navController.popBackStack() }
                        )
                    }
                }

            }
        }
    }
}