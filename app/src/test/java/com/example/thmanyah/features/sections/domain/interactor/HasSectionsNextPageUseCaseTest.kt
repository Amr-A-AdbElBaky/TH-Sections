package com.example.thmanyah.features.sections.domain.interactor


import com.example.thmanyah.features.sections.domain.interactors.HasSectionsNextPageUseCase
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import com.example.thmanyah.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HasSectionsNextPageUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: SectionsRepository = mock()
    private val useCase = HasSectionsNextPageUseCase(repository)

    @Test
    fun `invoke should return true when repository has next page`() = runTest {
        whenever(repository.hasNextPage()).thenReturn(flowOf(true))

        val result = useCase().first()

        verify(repository).hasNextPage()
        assertTrue(result)
    }

    @Test
    fun `invoke should return false when repository has no next page`() = runTest {
        whenever(repository.hasNextPage()).thenReturn(flowOf(false))

        val result = useCase().first()

        verify(repository).hasNextPage()
        assertFalse(result)
    }

    @Test
    fun `invoke should reflect state changes in repository`() = runTest {
        val stateFlow = MutableStateFlow(true)
        whenever(repository.hasNextPage()).thenReturn(stateFlow)

        // Collect in a separate coroutine
        val results = mutableListOf<Boolean>()
        val job = launch {
            useCase().collect { results.add(it) }
        }

        delay(10)
        stateFlow.emit(false)
        delay(10)
        stateFlow.emit(true)
        delay(10)
        job.cancel()

        assertEquals(listOf(true, false, true), results)
    }
}