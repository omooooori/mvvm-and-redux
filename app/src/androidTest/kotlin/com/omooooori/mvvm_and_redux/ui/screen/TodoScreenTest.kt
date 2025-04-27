package com.omooooori.mvvm_and_redux.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.omooooori.mvvm_and_redux.data.model.Todo
import com.omooooori.mvvm_and_redux.store.TodoState
import com.omooooori.mvvm_and_redux.ui.theme.MvvmandreduxTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class TodoScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<TodoViewModel>(relaxed = true)

    private fun setContent() {
        composeTestRule.setContent {
            MvvmandreduxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TodoListScreen(viewModel = mockViewModel)
                }
            }
        }
    }

    @Test
    fun 初期表示時にTodoリストが表示される() {
        val testTodos = listOf(
            Todo(
                id = 1,
                title = "Test Todo 1",
                description = "Test Description 1",
                createdAt = Date()
            ),
            Todo(
                id = 2,
                title = "Test Todo 2",
                description = "Test Description 2",
                createdAt = Date()
            )
        )
        val state = TodoState(todos = testTodos)
        every { mockViewModel.state } returns MutableStateFlow(state)

        setContent()

        composeTestRule.onNodeWithText("Test Todo 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Todo 2").assertIsDisplayed()
    }

    @Test
    fun ローディング中はプログレスバーが表示される() {
        val state = TodoState(isLoading = true)
        every { mockViewModel.state } returns MutableStateFlow(state)

        setContent()

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun エラー時はエラーメッセージが表示される() {
        val state = TodoState(error = "エラーが発生しました")
        every { mockViewModel.state } returns MutableStateFlow(state)

        setContent()

        composeTestRule.onNodeWithText("エラーが発生しました").assertIsDisplayed()
    }

    @Test
    fun Todo追加ボタンをタップするとダイアログが表示される() {
        val state = TodoState()
        every { mockViewModel.state } returns MutableStateFlow(state)

        setContent()

        composeTestRule.onNodeWithTag("addTodoButton").performClick()
        composeTestRule.onNodeWithText("新しいTODOを追加").assertIsDisplayed()
    }
}
