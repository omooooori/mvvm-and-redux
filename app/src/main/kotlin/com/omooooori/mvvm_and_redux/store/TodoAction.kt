package com.omooooori.mvvm_and_redux.store

import com.omooooori.mvvm_and_redux.data.model.Todo

sealed class TodoAction {
    data class AddTodo(val todo: Todo) : TodoAction()
    data class DeleteTodo(val id: Long) : TodoAction()
    data class SetTodos(val todos: List<Todo>) : TodoAction()
    data class SetLoading(val isLoading: Boolean) : TodoAction()
    data class SetError(val error: String?) : TodoAction()
} 