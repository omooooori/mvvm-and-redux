package com.omooooori.mvvm_and_redux.ui.screen

import androidx.lifecycle.ViewModel
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.store.TodoAction
import com.omooooori.mvvm_and_redux.store.TodoState
import com.omooooori.mvvm_and_redux.store.TodoStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val store: TodoStore
) : ViewModel() {
    val state: StateFlow<TodoState> = store.state

    fun addTodo(title: String, description: String) {
        val todo = Todo(
            title = title,
            description = description
        )
        store.dispatch(TodoAction.AddTodo(todo))
    }

    fun deleteTodo(id: Long) {
        store.dispatch(TodoAction.DeleteTodo(id))
    }
} 