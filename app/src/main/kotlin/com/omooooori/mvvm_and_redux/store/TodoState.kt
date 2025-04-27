package com.omooooori.mvvm_and_redux.store

import com.omooooori.mvvm_and_redux.data.model.Todo

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 