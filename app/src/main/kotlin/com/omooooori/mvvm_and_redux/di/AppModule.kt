package com.omooooori.mvvm_and_redux.di

import android.content.Context
import androidx.room.Room
import com.omooooori.mvvm_and_redux.data.db.TodoDatabase
import com.omooooori.mvvm_and_redux.data.db.TodoDao
import com.omooooori.mvvm_and_redux.data.repository.TodoRepository
import com.omooooori.mvvm_and_redux.store.TodoMiddleware
import com.omooooori.mvvm_and_redux.store.TodoStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase) = database.todoDao()

    @Provides
    @Singleton
    fun provideCoroutineScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideTodoMiddleware(
        repository: TodoRepository,
        coroutineScope: CoroutineScope
    ) = TodoMiddleware(repository, coroutineScope)

    @Provides
    @Singleton
    fun provideTodoStore(
        middleware: TodoMiddleware,
        coroutineScope: CoroutineScope
    ) = TodoStore(middleware, coroutineScope)
}
