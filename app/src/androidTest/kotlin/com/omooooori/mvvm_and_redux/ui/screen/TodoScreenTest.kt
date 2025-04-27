package com.omooooori.mvvm_and_redux.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.omooooori.mvvm_and_redux.data.model.Todo
import org.junit.Rule
import org.junit.Test
import java.util.Date

class TodoScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun todoListIsDisplayed() {
        val uiState = TodoListUiState(
            todos = listOf(
                Todo(
                    id = 1,
                    title = "Test Todo",
                    description = "Test Description",
                    createdAt = Date()
                )
            ),
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            TodoListScreen(
                uiState = uiState,
                onAddTodo = { _, _ -> },
                onDeleteTodo = { }
            )
        }

        composeTestRule.onNodeWithText("Test Todo").assertIsDisplayed()
    }

    @Test
    fun loadingIndicatorIsDisplayed() {
        val uiState = TodoListUiState(
            todos = emptyList(),
            isLoading = true,
            error = null
        )

        composeTestRule.setContent {
            TodoListScreen(
                uiState = uiState,
                onAddTodo = { _, _ -> },
                onDeleteTodo = { }
            )
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun errorMessageIsDisplayed() {
        val uiState = TodoListUiState(
            todos = emptyList(),
            isLoading = false,
            error = "Error occurred"
        )

        composeTestRule.setContent {
            TodoListScreen(
                uiState = uiState,
                onAddTodo = { _, _ -> },
                onDeleteTodo = { }
            )
        }

        composeTestRule.onNodeWithText("Error occurred").assertIsDisplayed()
    }

    @Test
    fun addTodoDialogIsDisplayed() {
        val uiState = TodoListUiState(
            todos = emptyList(),
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            TodoListScreen(
                uiState = uiState,
                onAddTodo = { _, _ -> },
                onDeleteTodo = { }
            )
        }

        composeTestRule.onNodeWithTag("addTodoButton").performClick()
        composeTestRule.onNodeWithText("新しいTODOを追加").assertIsDisplayed()
    }
}
