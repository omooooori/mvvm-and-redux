package com.omooooori.mvvm_and_redux.store

import app.cash.turbine.test
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.data.repository.TodoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class TodoMiddlewareTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val testScope = CoroutineScope(testDispatcher)
    val repository = mockk<TodoRepository>()
    val actionsFlow = MutableSharedFlow<TodoAction>()
    val middleware = TodoMiddleware(repository, testScope)

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("TodoMiddleware") {
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description"
        )

        When("AddTodoアクションがディスパッチされた時") {
            coEvery { repository.addTodo(todo) } returns Unit

            Then("リポジトリのaddTodoが呼ばれる") {
                runTest {
                    middleware.dispatch(TodoAction.AddTodo(todo))
                    testDispatcher.scheduler.advanceUntilIdle()
                    coVerify { repository.addTodo(todo) }
                }
            }
        }

        When("DeleteTodoアクションがディスパッチされた時") {
            coEvery { repository.deleteTodo(1) } returns Unit

            Then("リポジトリのdeleteTodoが呼ばれる") {
                runTest {
                    middleware.dispatch(TodoAction.DeleteTodo(1))
                    testDispatcher.scheduler.advanceUntilIdle()
                    coVerify { repository.deleteTodo(1) }
                }
            }
        }

        When("エラーが発生した時") {
            val error = Exception("Test Error")
            coEvery { repository.addTodo(todo) } throws error

            Then("SetErrorアクションが発行される") {
                runTest {
                    middleware.actions.test {
                        middleware.dispatch(TodoAction.AddTodo(todo))
                        testDispatcher.scheduler.advanceUntilIdle()
                        val action = awaitItem()
                        action shouldBe TodoAction.SetError(error.message)
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }

        When("observeTodosが呼ばれた時") {
            val todos = listOf(todo)
            coEvery { repository.getAllTodos() } returns flowOf(todos)

            Then("リポジトリのgetAllTodosが呼ばれる") {
                runTest {
                    middleware.observeTodos().test {
                        val result = awaitItem()
                        result shouldBe todos
                        cancelAndConsumeRemainingEvents()
                        coVerify { repository.getAllTodos() }
                    }
                }
            }
        }
    }
}) 