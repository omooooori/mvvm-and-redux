package com.omooooori.mvvm_and_redux.store

import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.data.repository.TodoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class TodoMiddlewareTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val repository = mockk<TodoRepository>()
    val middleware = TodoMiddleware(repository, testDispatcher)

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
                    middleware.dispatch(TodoAction.AddTodo(todo))
                    testDispatcher.scheduler.advanceUntilIdle()
                    middleware.actions.collect { action ->
                        if (action is TodoAction.SetError) {
                            action.error shouldBe error.message
                        }
                    }
                }
            }
        }

        When("observeTodosが呼ばれた時") {
            val todos = listOf(todo)
            coEvery { repository.getAllTodos() } returns flowOf(todos)

            Then("リポジトリのgetAllTodosが呼ばれる") {
                runTest {
                    middleware.observeTodos().collect { result ->
                        result shouldBe todos
                    }
                    coVerify { repository.getAllTodos() }
                }
            }
        }
    }
}) 