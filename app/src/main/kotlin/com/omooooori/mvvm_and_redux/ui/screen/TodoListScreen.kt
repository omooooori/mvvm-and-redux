package com.omooooori.mvvm_and_redux.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omooooori.mvvm_and_redux.R
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.ui.component.TodoItem
import com.omooooori.mvvm_and_redux.ui.theme.MvvmandreduxTheme
import kotlinx.coroutines.launch
import java.util.Date

data class TodoListUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun TodoListScreen(
    uiState: TodoListUiState,
    onAddTodo: (String, String) -> Unit,
    onDeleteTodo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.testTag("addTodoButton")
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loadingIndicator")
                )
            } else {
                LazyColumn {
                    items(uiState.todos) { todo ->
                        TodoItem(
                            todo = todo,
                            onDelete = { onDeleteTodo(todo.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description ->
                onAddTodo(title, description)
                showAddDialog = false
            }
        )
    }

    uiState.error?.let { error ->
        scope.launch {
            snackbarHostState.showSnackbar(error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    MvvmandreduxTheme {
        TodoListScreen(
            uiState = TodoListUiState(
                todos = listOf(
                    Todo(
                        id = 1,
                        title = "Test Todo 1",
                        description = "Test Description 1",
                        createdAt = Date()
                    ),
                    Todo(
                        id = 2,
                        title = "Test Todo 2",
                        description = "Test Description 2",
                        createdAt = Date()
                    )
                )
            ),
            onAddTodo = { _, _ -> },
            onDeleteTodo = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenLoadingPreview() {
    MvvmandreduxTheme {
        TodoListScreen(
            uiState = TodoListUiState(isLoading = true),
            onAddTodo = { _, _ -> },
            onDeleteTodo = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenErrorPreview() {
    MvvmandreduxTheme {
        TodoListScreen(
            uiState = TodoListUiState(error = "エラーが発生しました"),
            onAddTodo = { _, _ -> },
            onDeleteTodo = { }
        )
    }
} 