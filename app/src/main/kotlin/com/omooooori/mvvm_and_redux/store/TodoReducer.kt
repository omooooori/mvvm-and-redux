package com.omooooori.mvvm_and_redux.store

fun todoReducer(state: TodoState, action: TodoAction): TodoState = when (action) {
    is TodoAction.AddTodo -> state.copy(
        todos = state.todos + action.todo
    )
    is TodoAction.DeleteTodo -> state.copy(
        todos = state.todos.filter { it.id != action.id }
    )
    is TodoAction.SetTodos -> state.copy(
        todos = action.todos
    )
    is TodoAction.SetLoading -> state.copy(
        isLoading = action.isLoading
    )
    is TodoAction.SetError -> state.copy(
        error = action.error
    )
}
