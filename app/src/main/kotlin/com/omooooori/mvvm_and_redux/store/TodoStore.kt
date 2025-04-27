package com.omooooori.mvvm_and_redux.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoStore @Inject constructor(
    private val middleware: TodoMiddleware,
    private val coroutineScope: CoroutineScope
) {
    private val _state = MutableStateFlow(TodoState())
    val state: StateFlow<TodoState> = _state.asStateFlow()

    init {
        coroutineScope.launch {
            middleware.actions.collect { action ->
                _state.value = todoReducer(_state.value, action)
            }
        }

        coroutineScope.launch {
            middleware.observeTodos().collect { todos ->
                dispatch(TodoAction.SetTodos(todos))
            }
        }
    }

    fun dispatch(action: TodoAction) {
        middleware.dispatch(action)
    }
}
