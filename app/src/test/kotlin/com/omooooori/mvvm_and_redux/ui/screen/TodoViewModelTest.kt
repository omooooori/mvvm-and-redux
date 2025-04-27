package com.omooooori.mvvm_and_redux.ui.screen

import app.cash.turbine.test
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.store.TodoAction
import com.omooooori.mvvm_and_redux.store.TodoState
import com.omooooori.mvvm_and_redux.store.TodoStore
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import java.util.Date

@ExperimentalCoroutinesApi
class TodoViewModelTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()
    val mockStore = mockk<TodoStore>()
    val testDate = Date()
    val testState = TodoState(
        todos = listOf(
            Todo(
                id = 1,
                title = "Test Todo",
                description = "Test Description",
                createdAt = testDate
            )
        ),
        isLoading = false,
        error = null
    )
    val stateFlow = MutableStateFlow(testState)
    every { mockStore.state } returns stateFlow
    every { mockStore.dispatch(any()) } returns Unit

    val viewModel = TodoViewModel(mockStore)

    Given("TodoViewModel") {
        When("初期化された時") {
            Then("Storeのstateが正しく反映される") {
                runTest {
                    viewModel.state.test {
                        val state = awaitItem()
                        state.todos.size shouldBe testState.todos.size
                        state.todos[0].id shouldBe testState.todos[0].id
                        state.todos[0].title shouldBe testState.todos[0].title
                        state.todos[0].description shouldBe testState.todos[0].description
                        state.isLoading shouldBe testState.isLoading
                        state.error shouldBe testState.error
                        cancelAndConsumeRemainingEvents()
                    }
                }
            }
        }

        When("addTodoが呼ばれた時") {
            val title = "New Todo"
            val description = "New Description"

            Then("StoreにAddTodoアクションがディスパッチされる") {
                viewModel.addTodo(title, description)
                verify { mockStore.dispatch(withArg<TodoAction.AddTodo> { action ->
                    action.todo.title shouldBe title
                    action.todo.description shouldBe description
                }) }
            }
        }

        When("deleteTodoが呼ばれた時") {
            val id = 1L

            Then("StoreにDeleteTodoアクションがディスパッチされる") {
                viewModel.deleteTodo(id)
                verify { mockStore.dispatch(TodoAction.DeleteTodo(id)) }
            }
        }
    }
}) 