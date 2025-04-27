package com.omooooori.mvvm_and_redux.data.repository

import com.omooooori.mvvm_and_redux.data.db.TodoDao
import com.omooooori.mvvm_and_redux.data.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun addTodo(todo: Todo) = todoDao.insertTodo(todo)

    suspend fun deleteTodo(id: Long) = todoDao.deleteTodo(id)
} 