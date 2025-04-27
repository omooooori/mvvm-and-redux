package com.omooooori.mvvm_and_redux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.omooooori.mvvm_and_redux.ui.screen.TodoListScreen
import com.omooooori.mvvm_and_redux.ui.screen.TodoListUiState
import com.omooooori.mvvm_and_redux.ui.screen.TodoViewModel
import com.omooooori.mvvm_and_redux.ui.theme.MvvmandreduxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MvvmandreduxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.state.collectAsState()
                    TodoListScreen(
                        uiState = TodoListUiState(
                            todos = state.todos,
                            isLoading = state.isLoading,
                            error = state.error
                        ),
                        onAddTodo = viewModel::addTodo,
                        onDeleteTodo = viewModel::deleteTodo
                    )
                }
            }
        }
    }
}