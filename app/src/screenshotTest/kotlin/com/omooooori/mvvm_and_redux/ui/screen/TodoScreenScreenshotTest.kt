package com.omooooori.mvvm_and_redux.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class TodoScreenScreenshotTest {
    @Preview
    @Composable
    fun TodoListScreenPreviewTest() {
        TodoListScreenPreview()
    }

    @Preview
    @Composable
    fun TodoListScreenLoadingPreviewTest() {
        TodoListScreenLoadingPreview()
    }

    @Preview
    @Composable
    fun TodoListScreenErrorPreviewTest() {
        TodoListScreenErrorPreview()
    }
}
