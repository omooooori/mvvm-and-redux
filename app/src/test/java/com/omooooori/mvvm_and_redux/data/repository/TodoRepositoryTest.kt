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
    val todoDao = mockk<TodoDao>()
    val repository = TodoRepository(todoDao)

    Given("TodoRepository") {
        val todo = Todo(
            id = 1,
            title = "Test Todo",
            description = "Test Description"
        )

        When("getAllTodosが呼ばれた時") {
            val todos = listOf(todo)
            coEvery { todoDao.getAllTodos() } returns flowOf(todos)

            Then("正しいTodoリストが返される") {
                runTest {
                    repository.getAllTodos().collect { result ->
                        result shouldBe todos
                    }
                }
                coVerify { todoDao.getAllTodos() }
            }
        }

        When("addTodoが呼ばれた時") {
            coEvery { todoDao.insertTodo(todo) } returns Unit

            Then("Todoが正しく追加される") {
                runTest {
                    repository.addTodo(todo)
                    coVerify { todoDao.insertTodo(todo) }
                }
            }
        }

        When("deleteTodoが呼ばれた時") {
            coEvery { todoDao.deleteTodo(1) } returns Unit

            Then("Todoが正しく削除される") {
                runTest {
                    repository.deleteTodo(1)
                    coVerify { todoDao.deleteTodo(1) }
                }
            }
        }
    }
}) 