package com.example.thmanyah.features.sections.data.repository


import app.cash.turbine.test
import com.example.thmanyah.core.data.model.PaginationModel
import com.example.thmanyah.features.sections.data.model.HomeResponse
import com.example.thmanyah.features.sections.data.model.SectionContentResponse
import com.example.thmanyah.features.sections.data.model.SectionResponse
import com.example.thmanyah.features.sections.data.source.remote.SectionsApi
import com.example.thmanyah.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SectionsRepositoryImplTest {

        @get:Rule
        val mainCoroutineRule = MainCoroutineRule()

        private lateinit var sectionsApi: SectionsApi
        private lateinit var repository: SectionsRepositoryImpl
        private val testDispatcher = UnconfinedTestDispatcher()

        @Before
        fun setup() {
            sectionsApi = mock()
            repository = SectionsRepositoryImpl(sectionsApi, testDispatcher)
        }

    @Test
    fun `getHomeSections with first page should reset pagination and return mapped sections`() = runTest {
        // Given
        val mockResponse = createMockHomeResponse(hasNextPage = true, totalPages = 3, currentPage = 1)
        whenever(sectionsApi.getHomeSections(page = 1, name = null)).thenReturn(mockResponse)

        // When
        val result = repository.getHomeSections(isFirstPage = true).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Section 1", result[0].name)
        assertEquals("Section 2", result[1].name)

        // Check if hasNextPage is true
        repository.hasNextPage().test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getHomeSections with subsequent page should increment page and return data`() = runTest {
        // Setup first page
        val mockFirstResponse = createMockHomeResponse(hasNextPage = true, totalPages = 3, currentPage = 1)
        whenever(sectionsApi.getHomeSections(page = 1, name = null)).thenReturn(mockFirstResponse)
        repository.getHomeSections(isFirstPage = true).first()

        // Given - second page
        val mockSecondResponse = createMockHomeResponse(hasNextPage = true, totalPages = 3, currentPage = 2)
        whenever(sectionsApi.getHomeSections(page = 2, name = null)).thenReturn(mockSecondResponse)

        // When
        val result = repository.getHomeSections(isFirstPage = false).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Section 1", result[0].name)
        assertEquals("Section 2", result[1].name)

        // Check if hasNextPage is still true
        repository.hasNextPage().test {
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getHomeSections should emit false for hasNextPage when nextPage is null`() = runTest {
        // Given
        val mockResponse = createMockHomeResponse(hasNextPage = false, totalPages = 1, currentPage = 1)
        whenever(sectionsApi.getHomeSections(page = 1, name = null)).thenReturn(mockResponse)

        // When
        repository.getHomeSections(isFirstPage = true).first()

        // Then
        repository.hasNextPage().test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getHomeSections should emit false when current page equals total pages`() = runTest {
        // Given
        // Create a mock response where the current page is the last page
        val mockResponse = createMockHomeResponse(hasNextPage = false, totalPages = 1, currentPage = 1)
        whenever(sectionsApi.getHomeSections(page = 1, name = null)).thenReturn(mockResponse)

        // When
        repository.getHomeSections(isFirstPage = true).first()

        // Then
        repository.hasNextPage().test {
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchForSections should return filtered sections based on query`() = runTest {
        // Given
        val searchQuery = "podcast"
        val mockResponse = createMockHomeResponse(hasNextPage = true, totalPages = 1, currentPage = 1)
        whenever(sectionsApi.getHomeSections(page = 1, name = searchQuery)).thenReturn(mockResponse)

        // When
        val result = repository.searchForSections(searchQuery).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Section 1", result[0].name)
        assertEquals("Section 2", result[1].name)
    }

    // Helper method to create mock responses
    private fun createMockHomeResponse(
        hasNextPage: Boolean,
        totalPages: Int,
        currentPage: Int
    ): HomeResponse {
        val pagination = PaginationModel(
            totalPages = totalPages,
            nextPage = if (hasNextPage) (currentPage + 1).toString() else null
        )

        val sectionContent = listOf(
            SectionContentResponse(
                name = "Content 1",
                description = "Description 1",
                avatarUrl = "https://example.com/avatar1.jpg",
                duration = 3600,
                language = "en",
                score = 4.5,
                podcastId = "podcast-id-1",
                episodeCount = 10,
                priority = 1,
                popularityScore = 100,
                episodeId = null,
                podcastPopularityScore = null,
                podcastPriority = null,
                seasonNumber = null,
                episodeType = null,
                podcastName = null,
                authorName = "Author 1",
                number = null,
                separatedAudioUrl = null,
                audioUrl = null,
                releaseDate = "2023-01-01",
                chapters = null
            )
        )

        val sections = listOf(
            SectionResponse(
                name = "Section 1",
                type = "SQUARE",
                contentType = "PODCAST",
                order = 1,
                content = sectionContent
            ),
            SectionResponse(
                name = "Section 2",
                type = "QUEUE",
                contentType = "EPISODE",
                order = 2,
                content = sectionContent
            )
        )

        return HomeResponse(sections = sections, pagination = pagination)
    }
}