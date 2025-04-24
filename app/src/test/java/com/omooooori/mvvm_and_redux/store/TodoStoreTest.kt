package com.omooooori.mvvm_and_redux.store

import app.cash.turbine.test
import com.omooooori.mvvm_and_redux.data.model.Todo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class TodoStoreTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val middleware = mockk<TodoMiddleware>()
    val store = TodoStore(middleware, testDispatcher)

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
                    }
                }
            }
        }

        When("SetTodosアクションがディスパッチされた時") {
            val todos = listOf(todo)
            coEvery { middleware.observeTodos() } returns flowOf(todos)

            Then("状態が更新される") {
                runTest {
                    store.state.test {
                        awaitItem() // 初期状態
                        val updatedState = awaitItem()
                        updatedState.todos shouldBe todos
                    }
                }
            }
        }

        When("SetLoadingアクションがディスパッチされた時") {
            Then("isLoadingが更新される") {
                runTest {
                    store.dispatch(TodoAction.SetLoading(true))
                    store.state.test {
                        awaitItem() // 初期状態
                        val updatedState = awaitItem()
                        updatedState.isLoading shouldBe true
                    }
                }
            }
        }

        When("SetErrorアクションがディスパッチされた時") {
            val error = "Test Error"
            Then("errorが更新される") {
                runTest {
                    store.dispatch(TodoAction.SetError(error))
                    store.state.test {
                        awaitItem() // 初期状態
                        val updatedState = awaitItem()
                        updatedState.error shouldBe error
                    }
                }
            }
        }
    }
}) 