package com.omooooori.mvvm_and_redux.ui.screen

import app.cash.turbine.test
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.store.TodoAction
import com.omooooori.mvvm_and_redux.store.TodoStore
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class TodoViewModelTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val store = mockk<TodoStore>()
    val viewModel = TodoViewModel(store)

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("TodoViewModel") {
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description"
        )

        When("初期状態") {
            coEvery { store.state } returns MutableStateFlow(TodoState())

            Then("正しい初期状態が設定される") {
                runTest {
                    viewModel.state.test {
                        val initialState = awaitItem()
                        initialState.todos shouldBe emptyList()
                        initialState.isLoading shouldBe false
                        initialState.error shouldBe null
                    }
                }
            }
        }

        When("addTodoが呼ばれた時") {
            Then("AddTodoアクションがディスパッチされる") {
                runTest {
                    viewModel.addTodo("Test Todo", "Test Description")
                    coVerify {
                        store.dispatch(match { action ->
                            action is TodoAction.AddTodo &&
                            action.todo.title == "Test Todo" &&
                            action.todo.description == "Test Description"
                        })
                    }
                }
            }
        }

        When("deleteTodoが呼ばれた時") {
            Then("DeleteTodoアクションがディスパッチされる") {
                runTest {
                    viewModel.deleteTodo(1)
                    coVerify {
                        store.dispatch(match { action ->
                            action is TodoAction.DeleteTodo && action.id == 1L
                        })
                    }
                }
            }
        }
    }
}) 