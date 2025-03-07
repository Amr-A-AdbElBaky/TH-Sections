package com.example.thmanyah.features.sections.domain.interactor


import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionContentEntity
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.domain.interactors.SearchForSectionsUseCase
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import com.example.thmanyah.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SearchForSectionsUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: SectionsRepository = mock()
    private val useCase = SearchForSectionsUseCase(repository)

    @Test
    fun `invoke should delegate to repository with search query`() = runTest {
        val searchQuery = "tech"
        val mockSections = createMockSections()
        whenever(repository.searchForSections(searchQuery)).thenReturn(flowOf(mockSections))

        val result = useCase(searchQuery).first()

        verify(repository).searchForSections(searchQuery)
        assertEquals(mockSections, result)
    }

    @Test
    fun `invoke with empty query should still work correctly`() = runTest {
        val searchQuery = ""
        val mockSections = createMockSections()
        whenever(repository.searchForSections(searchQuery)).thenReturn(flowOf(mockSections))

        val result = useCase(searchQuery).first()

        verify(repository).searchForSections(searchQuery)
        assertEquals(mockSections, result)
    }

    @Test
    fun `invoke should handle special characters in search query`() = runTest {
        val specialQuery = "test#$%&"
        whenever(repository.searchForSections(specialQuery)).thenReturn(flowOf(emptyList()))

        val result = useCase(specialQuery).first()

        verify(repository).searchForSections(specialQuery)
        assertTrue(result.isEmpty())
    }

    private fun createMockSections(): List<SectionEntity> {
        val contentEntity = SectionContentEntity(
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

        return listOf(
            SectionEntity(
                name = "Search Results",
                type = SectionType.SQUARE,
                contentType = ContentType.PODCAST,
                order = 1,
                content = listOf(contentEntity)
            )
        )
    }
}