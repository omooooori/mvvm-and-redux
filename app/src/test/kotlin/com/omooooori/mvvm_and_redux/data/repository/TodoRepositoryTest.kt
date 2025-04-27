package com.omooooori.mvvm_and_redux.data.repository

import com.omooooori.mvvm_and_redux.data.db.TodoDao
import com.omooooori.mvvm_and_redux.data.model.Todo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class TodoRepositoryTest : BehaviorSpec({
    val mockTodoDao = mockk<TodoDao>()
    val repository = TodoRepository(mockTodoDao)

    val testTodo = Todo(
        id = 1,
        title = "Test Todo",
        description = "Test Description"
    )

    Given("TodoRepository") {
        When("getAllTodosが呼ばれた時") {
            val expectedTodos = listOf(testTodo)
            coEvery { mockTodoDao.getAllTodos() } returns flowOf(expectedTodos)

            Then("DaoのgetAllTodosが呼ばれ、正しい結果が返される") {
                repository.getAllTodos().collect { todos ->
                    todos shouldBe expectedTodos
                }
                coVerify { mockTodoDao.getAllTodos() }
            }
        }

        When("addTodoが呼ばれた時") {
            coEvery { mockTodoDao.insertTodo(testTodo) } returns Unit

            Then("DaoのinsertTodoが呼ばれる") {
                repository.addTodo(testTodo)
                coVerify { mockTodoDao.insertTodo(testTodo) }
            }
        }

        When("deleteTodoが呼ばれた時") {
            coEvery { mockTodoDao.deleteTodo(1L) } returns Unit

            Then("DaoのdeleteTodoが呼ばれる") {
                repository.deleteTodo(1L)
                coVerify { mockTodoDao.deleteTodo(1L) }
            }
        }
    }
}) 