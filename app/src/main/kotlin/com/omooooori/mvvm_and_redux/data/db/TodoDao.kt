package com.omooooori.mvvm_and_redux.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.omooooori.mvvm_and_redux.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodo(id: Long)
} 