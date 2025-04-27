package com.omooooori.mvvm_and_redux.store

import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.data.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoMiddleware @Inject constructor(
    private val repository: TodoRepository,
    private val coroutineScope: CoroutineScope
) {
    private val _actions = MutableSharedFlow<TodoAction>()
    val actions: SharedFlow<TodoAction> = _actions

    fun dispatch(action: TodoAction) {
        coroutineScope.launch {
            when (action) {
                is TodoAction.AddTodo -> {
                    try {
                        repository.addTodo(action.todo)
                    } catch (e: Exception) {
                        _actions.emit(TodoAction.SetError(e.message))
                    }
                }
                is TodoAction.DeleteTodo -> {
                    try {
                        repository.deleteTodo(action.id)
                    } catch (e: Exception) {
                        _actions.emit(TodoAction.SetError(e.message))
                    }
                }
                else -> _actions.emit(action)
            }
        }
    }

    fun observeTodos(): Flow<List<Todo>> = repository.getAllTodos()
}
