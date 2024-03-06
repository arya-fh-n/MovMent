package com.arfdevs.myproject.movment.presentation.viewmodel

import com.arfdevs.myproject.core.domain.model.User
import com.arfdevs.myproject.core.domain.usecase.AppUseCase
import com.arfdevs.myproject.core.helper.UiState
import com.arfdevs.myproject.core.helper.onError
import com.arfdevs.myproject.core.helper.onLoading
import com.arfdevs.myproject.core.helper.onSuccess
import com.arfdevs.myproject.movment.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    lateinit var useCase: AppUseCase

    @Mock
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authViewModel = AuthViewModel(useCase)
    }

    @Test
    fun `test registerUser success`() = runTest {
        val user = User("penggunabaru@gmail.com", "12345678")

        val expected = true
        val mockFlow = flowOf(UiState.Success(expected))

        authViewModel.registerUser(user)
        whenever(useCase.createUser(user)).thenReturn(mockFlow)
        verify(useCase).createUser(user)

        val result = mutableListOf<Boolean>()
        authViewModel.registerUser(user).collect {
            it.onSuccess {
                result.add(it)
            }.onError {
                result.add(false)
            }.onLoading {
                result.add(false)
            }
        }

        assertEquals(expected, result.last())
    }

    @Test
    fun `test registerUser failed`() = runTest {
        val user = User("penggunabaru@gmail.com", "password")

        val expected = Throwable()
        val mockFlow = flowOf(UiState.Error(expected))

        authViewModel.registerUser(user)
        whenever(useCase.createUser(user)).thenReturn(mockFlow)
        verify(useCase).createUser(user)

        authViewModel.registerUser(user).collect {
            it.onError { e ->
                assertEquals(expected, e)
            }
        }
    }
}
