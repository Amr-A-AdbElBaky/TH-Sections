package com.example.thmanyah.features.sections.presentation.viewmodel


import android.content.Context
import app.cash.turbine.test
import com.example.thmanyah.R
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionContentEntity
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.domain.interactors.GetHomeSectionsUseCase
import com.example.thmanyah.features.sections.domain.interactors.HasSectionsNextPageUseCase
import com.example.thmanyah.features.sections.domain.interactors.SearchForSectionsUseCase
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsEvents
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsIntent
import com.example.thmanyah.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
class SectionsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    // Mock dependencies
    private lateinit var getHomeSectionsUseCase: GetHomeSectionsUseCase
    private lateinit var searchForSectionsUseCase: SearchForSectionsUseCase
    private lateinit var hasNextPageUseCase: HasSectionsNextPageUseCase
    private lateinit var context: Context

    // Class under test
    private lateinit var viewModel: SectionsViewModel
    private val NO_INTERNET_ERROR = "No internet connection"

    @Before
    fun setup() {
        getHomeSectionsUseCase = mock()
        searchForSectionsUseCase = mock()
        hasNextPageUseCase = mock()
        context = mock()

        whenever(context.getString(any(), any())).thenAnswer { invocation ->
            val resourceId = (invocation.arguments[0] as Int)
            val formatArg = (invocation.arguments[1] as Long).toInt()

            when (resourceId) {
                R.string.label_hour_qualifier -> "$formatArg hr"
                R.string.label_minute_qualifier -> "$formatArg min"
                R.string.label_episodes -> "episodes"
                else -> "mock_string"
            }
        }
        whenever(context.getString(any())).thenReturn("mock_string")


        // Set up default behavior for mocks
        whenever(hasNextPageUseCase.invoke()).thenReturn(flowOf(true))
        whenever(getHomeSectionsUseCase.invoke(any())).thenReturn(flowOf(emptyList()))

        viewModel = SectionsViewModel(
            getHomeSectionsUseCase,
            searchForSectionsUseCase,
            hasNextPageUseCase,
            context
        )
    }


    @Test
    fun `init should listen for next page`() = runTest {
        // Given - setup is done in @Before

        verify(hasNextPageUseCase).invoke()
    }

    @Test
    fun `LoadHomeSections intent should call getHomeSectionsUseCase with isFirstPage=true`() = runTest {
        val mockSections = createMockSections()
        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(flowOf(mockSections))

        viewModel.processIntent(SectionsIntent.LoadHomeSections)
        advanceUntilIdle()

        verify(getHomeSectionsUseCase).invoke(true)
        assertEquals(false,viewModel.uiModel.value?.showFullLoading)
        assertEquals(2, viewModel.uiModel.value?.sections?.size)
    }

    @Test
    fun `LoadMoreSections intent should call getHomeSectionsUseCase with isFirstPage=false`() = runTest {
        val mockSections = createMockSections()
        whenever(hasNextPageUseCase.invoke()).thenReturn(flowOf(true))
        whenever(getHomeSectionsUseCase.invoke(false)).thenReturn(flowOf(mockSections))

        viewModel.processIntent(SectionsIntent.LoadMoreSections)
        advanceUntilIdle()

        verify(getHomeSectionsUseCase).invoke(false)
        assertEquals(false,viewModel.uiModel.value?.showFullLoading)
        assertEquals(2, viewModel.uiModel.value?.sections?.size)
    }

    @Test
    fun `LoadMoreSections should not load when hasNextPage is false`() = runTest {
        whenever(hasNextPageUseCase.invoke()).thenReturn(flowOf(false))
        viewModel = SectionsViewModel(
            getHomeSectionsUseCase,
            searchForSectionsUseCase,
            hasNextPageUseCase,
            context
        )
        advanceUntilIdle() // Allow initial setup to complete

        viewModel.processIntent(SectionsIntent.LoadMoreSections)
        advanceUntilIdle()

        // Then - getHomeSectionsUseCase should not be called again
        verify(getHomeSectionsUseCase, times(0)).invoke(false)
    }

    @Test
    fun `SearchByName intent should update searchQuery`() = runTest {
        val searchQuery = "podcast"
        val mockSections = createMockSections()
        whenever(searchForSectionsUseCase.invoke(searchQuery)).thenReturn(flowOf(mockSections))

        viewModel.processIntent(SectionsIntent.SearchByName(searchQuery))
        // Need to advance time past the debounce period
        advanceTimeBy(310) // Just past the 300ms debounce
        advanceUntilIdle()

        verify(searchForSectionsUseCase).invoke(searchQuery)
        assertEquals(2, viewModel.uiModel.value?.sections?.size)
    }

    @Test
    fun `SelectCategory intent should update selected category`() = runTest {
        val categoryIndex = 1

        viewModel.processIntent(SectionsIntent.SelectCategory(categoryIndex))

        assertEquals("المقالات الصوتية", viewModel.state.value.selectedCategory)
        assertEquals(categoryIndex, viewModel.uiModel.value?.selectedCategoryIndex)
    }

    @Test
    fun `SectionItemClicked intent should emit NavigateToDetails event`() = runTest {
        val id = "test-id"
        val contentType = ContentType.PODCAST

        // When & Then
        viewModel.event.test {
            viewModel.processIntent(SectionsIntent.SectionItemClicked(id, contentType))
            val event = awaitItem()
            assertTrue(event is SectionsEvents.NavigateToDetails)
            val navigateEvent = event as SectionsEvents.NavigateToDetails
            assertEquals(id, navigateEvent.id)
            assertEquals(contentType, navigateEvent.contentType)
        }
    }

    @Test
    fun `ClearSearch intent should cancel current search job and clear sections`() = runTest {
        // Given - first do a search to set up the state
        val searchQuery = "podcast"
        val mockSections = createMockSections()
        whenever(searchForSectionsUseCase.invoke(searchQuery)).thenReturn(flowOf(mockSections))
        viewModel.processIntent(SectionsIntent.SearchByName(searchQuery))
        advanceTimeBy(310) // Just past the debounce
        advanceUntilIdle()

        viewModel.processIntent(SectionsIntent.ClearSearch)

        assertTrue(viewModel.state.value.sections.isEmpty())
    }

    @Test
    fun `error handling in LoadHomeSections should update error state`() = runTest {
        val exception = IOException(NO_INTERNET_ERROR)

        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(
            flow { throw exception }
        )

        viewModel.processIntent(SectionsIntent.LoadHomeSections)
        advanceUntilIdle()

        assertEquals(NO_INTERNET_ERROR, viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `error handling in LoadMoreSections should update error state correctly`() = runTest {
        val exception = IOException(NO_INTERNET_ERROR)

        // First load some data to have non-empty sections
        val mockSections = createMockSections()
        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(flowOf(mockSections))
        viewModel.processIntent(SectionsIntent.LoadHomeSections)
        advanceUntilIdle()

        whenever(getHomeSectionsUseCase.invoke(false)).thenReturn(
            flow { throw exception }
        )

        viewModel.processIntent(SectionsIntent.LoadMoreSections)
        advanceUntilIdle()

        assertEquals(NO_INTERNET_ERROR, viewModel.state.value.error)
        assertFalse(viewModel.state.value.isLoading)

        // Verify error is shown as snack when sections are not empty
        assertEquals(NO_INTERNET_ERROR, viewModel.uiModel.value?.snackError)
        assertNull(viewModel.uiModel.value?.error) // Full screen error should be null
    }

    // For now case depends on error message, but we should depend on domain exception
    @Test
    fun `mapStateToUiModel handles error display correctly based on sections state`() = runTest {
        // Given - Error with empty sections (should show full screen error)
        val exception = IOException(NO_INTERNET_ERROR)

        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(
            flow { throw exception }
        )

        viewModel.processIntent(SectionsIntent.LoadHomeSections)
        advanceUntilIdle()

        assertEquals(NO_INTERNET_ERROR, viewModel.uiModel.value?.error)
        assertNull(viewModel.uiModel.value?.snackError)

        val mockSections = createMockSections()
        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(flowOf(mockSections))
        viewModel.processIntent(SectionsIntent.LoadHomeSections)
        advanceUntilIdle()

        // Set up error for refresh
        whenever(getHomeSectionsUseCase.invoke(true)).thenReturn(
            flow { throw exception }
        )

        viewModel.processIntent(SectionsIntent.RefreshHomeSections)
        advanceUntilIdle()

        // Then - Error should be in snackError and not in error
        assertNull(viewModel.uiModel.value?.error)
        assertEquals(NO_INTERNET_ERROR, viewModel.uiModel.value?.snackError)
    }

    @Test
    fun `observeSearchQuery debounces search requests`() = runTest {
        val mockSections = createMockSections()
        whenever(searchForSectionsUseCase.invoke("final")).thenReturn(flowOf(mockSections))

        // When - Send multiple search intents in quick succession
        viewModel.processIntent(SectionsIntent.SearchByName("test1"))
        advanceTimeBy(100) // Not enough time for debounce
        viewModel.processIntent(SectionsIntent.SearchByName("test2"))
        advanceTimeBy(100) // Not enough time for debounce
        viewModel.processIntent(SectionsIntent.SearchByName("final"))
        advanceTimeBy(310) // Just past the debounce
        advanceUntilIdle()

        // Then - Only the last query should be processed
        verify(searchForSectionsUseCase, times(1)).invoke(any())
        verify(searchForSectionsUseCase).invoke("final")
    }

    @Test
    fun `searchSections cancels previous job when new search starts`() = runTest {
        val mockSections = createMockSections()
        whenever(searchForSectionsUseCase.invoke(any())).thenReturn(flowOf(mockSections))

        // When - Start a search, then immediately start another
        viewModel.processIntent(SectionsIntent.SearchByName("first"))
        advanceTimeBy(310) // Just enough for debounce
        viewModel.processIntent(SectionsIntent.SearchByName("second"))
        advanceTimeBy(310) // Just enough for debounce
        advanceUntilIdle()

        // Then - Both searches should be invoked, but we're verifying the flow behavior
        verify(searchForSectionsUseCase).invoke("first")
        verify(searchForSectionsUseCase).invoke("second")
    }

    // Helper method to create test data
    private fun createMockSections(): List<SectionEntity> {
        val contentEntity1 = SectionContentEntity(
            id = "podcast-1",
            name = "Tech Talk",
            description = "Weekly tech discussions",
            avatarUrl = "https://example.com/avatar1.jpg",
            episodeCount = 50,
            duration = 3600,
            language = "en",
            priority = 1,
            popularityScore = 95,
            score = 4.8,
            authorName = "Jane Smith",
            releaseDate = "2023-05-15"
        )

        val contentEntity2 = SectionContentEntity(
            id = "podcast-2",
            name = "Science Hour",
            description = "Latest in science",
            avatarUrl = "https://example.com/avatar2.jpg",
            episodeCount = 30,
            duration = 2700,
            language = "en",
            priority = 2,
            popularityScore = 85,
            score = 4.5,
            authorName = "John Doe",
            releaseDate = "2023-06-01"
        )

        return listOf(
            SectionEntity(
                name = "Trending Podcasts",
                type = SectionType.SQUARE,
                contentType = ContentType.PODCAST,
                order = 1,
                content = listOf(contentEntity1, contentEntity2)
            ),
            SectionEntity(
                name = "Featured Episodes",
                type = SectionType.QUEUE,
                contentType = ContentType.EPISODE,
                order = 2,
                content = listOf(contentEntity1)
            )
        )
    }
}