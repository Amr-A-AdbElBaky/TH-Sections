package com.example.thmanyah.features.sections.domain.interactor


import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionContentEntity
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.domain.interactors.GetHomeSectionsUseCase
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import com.example.thmanyah.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
class GetHomeSectionsUseCaseTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: SectionsRepository = mock()
    private val useCase = GetHomeSectionsUseCase(repository)

    @Test
    fun `invoke with isFirstPage=true should delegate to repository and return the same result`() = runTest {
        val mockSections = createMockSections()
        whenever(repository.getHomeSections(isFirstPage = true)).thenReturn(flowOf(mockSections))

        val result = useCase(isFirstPage = true).first()

        verify(repository).getHomeSections(isFirstPage = true)
        assertEquals(mockSections, result)
        assertEquals(2, result.size)
        assertEquals("Trending Podcasts", result[0].name)
        assertEquals(SectionType.SQUARE, result[0].type)
        assertEquals(ContentType.PODCAST, result[0].contentType)
    }

    @Test
    fun `invoke with isFirstPage=false should delegate to repository with correct parameter`() = runTest {
        val mockSections = createMockSections()
        whenever(repository.getHomeSections(isFirstPage = false)).thenReturn(flowOf(mockSections))

        val result = useCase(isFirstPage = false).first()

        verify(repository).getHomeSections(isFirstPage = false)
        assertEquals(mockSections, result)
    }

    @Test
    fun `invoke should propagate repository errors`() = runTest {
        val networkException = IOException("Network error")
        whenever(repository.getHomeSections(isFirstPage = true)).thenReturn(flow { throw networkException })

        assertThrows(IOException::class.java) {
            runBlocking { useCase(isFirstPage = true).first() }
        }
    }

    @Test
    fun `invoke should handle empty results`() = runTest {
        whenever(repository.getHomeSections(isFirstPage = true)).thenReturn(flowOf(emptyList()))

        val result = useCase(isFirstPage = true).first()

        assertTrue(result.isEmpty())
    }


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