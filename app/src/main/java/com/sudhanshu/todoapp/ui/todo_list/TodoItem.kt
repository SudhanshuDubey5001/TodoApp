package com.sudhanshu.todoapp.ui.todo_list

import android.widget.ImageButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudhanshu.todoapp.data.Todo


@Composable
fun TodoItem(
    todo: Todo,
    event: (TodoListEvents) -> Unit,
    modifier: Modifier
) {
    Row {
        Column(
            modifier = Modifier
                .padding(all = 10.dp)
                .weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        event(TodoListEvents.OnDeleteTodoClick(todo))
                    }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "deleteIcon")
                }
            }
            Text(text = todo.description.toString(), maxLines = 3)
        }
        Checkbox(checked = todo.isDone, onCheckedChange = {
            event(TodoListEvents.OnDoneTodo(todo, todo.isDone))
        })
    }

}
//
//@Preview
//@Composable
//fun TodoItemPreview(){
//    TodoItem(todo = Todo("Get laundry done!","Gota take all the clothes and ge the washed",false, 3))
//}