package com.omooooori.mvvm_and_redux.store

import app.cash.turbine.test
import com.omooooori.mvvm_and_redux.data.model.Todo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
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
class TodoStoreTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val testScope = CoroutineScope(testDispatcher)
    val middleware = mockk<TodoMiddleware>()
    val actionsFlow = MutableSharedFlow<TodoAction>()
    
    every { middleware.actions } returns actionsFlow
    every { middleware.observeTodos() } returns flowOf(emptyList())
    every { middleware.dispatch(any()) } answers {
        val action = firstArg<TodoAction>()
        runTest {
            actionsFlow.emit(action)
        }
    }

    val store = TodoStore(middleware, testScope)

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("TodoStore") {
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description"
        )

        When("初期状態") {
            Then("正しい初期状態が設定される") {
                runTest {
                    store.state.test {
                        val initialState = awaitItem()
                        initialState.todos shouldBe emptyList()
                        initialState.isLoading shouldBe false
                        initialState.error shouldBe null
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }

        When("SetTodosアクションがディスパッチされた時") {
            val todos = listOf(todo)
            every { middleware.observeTodos() } returns flowOf(todos)

            Then("状態が更新される") {
                runTest {
                    store.state.test {
                        awaitItem() // 初期状態
                        store.dispatch(TodoAction.SetTodos(todos))
                        testDispatcher.scheduler.advanceUntilIdle()
                        val updatedState = awaitItem()
                        updatedState.todos shouldBe todos
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }

        When("SetLoadingアクションがディスパッチされた時") {
            Then("isLoadingが更新される") {
                runTest {
                    store.state.test {
                        awaitItem() // 初期状態
                        store.dispatch(TodoAction.SetLoading(true))
                        testDispatcher.scheduler.advanceUntilIdle()
                        val updatedState = awaitItem()
                        updatedState.isLoading shouldBe true
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }

        When("SetErrorアクションがディスパッチされた時") {
            val errorMessage = "Test Error"
            Then("errorが更新される") {
                runTest {
                    store.state.test {
                        awaitItem() // 初期状態
                        store.dispatch(TodoAction.SetError(errorMessage))
                        testDispatcher.scheduler.advanceUntilIdle()
                        val updatedState = awaitItem()
                        updatedState.error shouldBe errorMessage
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }
    }
}) 