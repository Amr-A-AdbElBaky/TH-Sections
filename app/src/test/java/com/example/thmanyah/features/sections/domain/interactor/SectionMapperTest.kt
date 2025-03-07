package com.example.thmanyah.features.sections.domain.interactor


import com.example.thmanyah.features.sections.data.model.Chapter
import com.example.thmanyah.features.sections.data.model.SectionContentResponse
import com.example.thmanyah.features.sections.data.model.SectionResponse
import com.example.thmanyah.features.sections.data.model.mapper.toSectionEntity
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SectionMapperTest {

    @Test
    fun `toSectionEntity should map podcast section correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Top Podcasts",
            type = "SQUARE",
            contentType = "podcast",
            order = 1,
            podcastId = "podcast-123"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Top Podcasts", result.name)
        assertEquals(SectionType.SQUARE, result.type)
        assertEquals(ContentType.PODCAST, result.contentType)
        assertEquals(1, result.order)

        val content = result.content.first()
        assertEquals("podcast-123", content.id)
        assertEquals("Podcast Title", content.name)
        assertEquals("Podcast Description", content.description)
        assertEquals("https://example.com/avatar.jpg", content.avatarUrl)
        assertEquals(10, content.episodeCount)
        assertEquals(3600L, content.duration)
        assertEquals("en", content.language)
        assertEquals(1, content.priority)
        assertEquals(95, content.popularityScore)
        assertEquals(4.5, content.score)
        assertEquals("Author Name", content.authorName)
        assertEquals("2023-01-01", content.releaseDate)
    }

    @Test
    fun `toSectionEntity should map episode section correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Latest Episodes",
            type = "QUEUE",
            contentType = "episode",
            order = 2,
            episodeId = "episode-456"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Latest Episodes", result.name)
        assertEquals(SectionType.QUEUE, result.type)
        assertEquals(ContentType.EPISODE, result.contentType)
        assertEquals(2, result.order)

        val content = result.content.first()
        assertEquals("episode-456", content.id)
    }

    @Test
    fun `toSectionEntity should map audio book section correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Popular Audiobooks",
            type = "BIG_SQUARE",
            contentType = "audio_book",
            order = 3,
            audiobookId = "book-789"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Popular Audiobooks", result.name)
        assertEquals(SectionType.BIG_SQUARE, result.type)
        assertEquals(ContentType.AUDIO_BOOK, result.contentType)

        val content = result.content.first()
        assertEquals("book-789", content.id)
    }

    @Test
    fun `toSectionEntity should map audio article section correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Featured Articles",
            type = "TWO_LINES_GRID",
            contentType = "audio_article",
            order = 4,
            articleId = "article-012"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Featured Articles", result.name)
        assertEquals(SectionType.TWO_LINES_GRID, result.type)
        assertEquals(ContentType.AUDIO_ARTICLE, result.contentType)

        val content = result.content.first()
        assertEquals("article-012", content.id)
    }

    @Test
    fun `toSectionEntity should handle unknown section types`() {
        val sectionResponse = createSectionResponse(
            name = "Custom Section",
            type = "custom_type",
            contentType = "custom_content",
            order = 5
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Custom Section", result.name)
        assertEquals(SectionType.UNKNOWN, result.type)
        assertEquals(ContentType.UNKNOWN, result.contentType)

        val content = result.content.first()
        assertEquals("", content.id) // Empty ID for unknown content type
    }

    @Test
    fun `toSectionEntity should handle null values`() {
        val contentResponse = SectionContentResponse(
            name = "Null Test",
            description = "Description",
            avatarUrl = "https://example.com/default.jpg",
            duration = 1800,
            language = null,
            score = null,
            podcastId = null,
            episodeCount = null,
            priority = null,
            popularityScore = null,
            episodeId = null,
            podcastPopularityScore = null,
            podcastPriority = null,
            seasonNumber = null,
            episodeType = null,
            podcastName = null,
            authorName = null,
            number = null,
            separatedAudioUrl = null,
            audioUrl = null,
            releaseDate = null,
            chapters = null,
            audiobookId = null,
            articleId = null
        )

        val sectionResponse = SectionResponse(
            name = "Null Section",
            type = "SQUARE",
            contentType = "podcast",
            order = 6,
            content = listOf(contentResponse)
        )

        val result = sectionResponse.toSectionEntity()

        val content = result.content.first()
        assertEquals("", content.id)
        assertEquals("", content.language)
        assertEquals(0, content.episodeCount)
        assertEquals(0, content.priority)
        assertEquals(0, content.popularityScore)
        assertNull(content.score)
        assertNull(content.authorName)
        assertNull(content.releaseDate)
    }

    @Test
    fun `toSectionEntity should handle multiple content items`() {
        val contentResponses = listOf(
            createContentResponse(name = "Content 1", podcastId = "podcast-1"),
            createContentResponse(name = "Content 2", podcastId = "podcast-2"),
            createContentResponse(name = "Content 3", podcastId = "podcast-3")
        )

        val sectionResponse = SectionResponse(
            name = "Multiple Content",
            type = "SQUARE",
            contentType = "podcast",
            order = 7,
            content = contentResponses
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals(3, result.content.size)
        assertEquals("Content 1", result.content[0].name)
        assertEquals("podcast-1", result.content[0].id)
        assertEquals("Content 2", result.content[1].name)
        assertEquals("podcast-2", result.content[1].id)
        assertEquals("Content 3", result.content[2].name)
        assertEquals("podcast-3", result.content[2].id)
    }

    @Test
    fun `toSectionEntity should handle empty content list`() {
        val sectionResponse = SectionResponse(
            name = "Empty Content",
            type = "SQUARE",
            contentType = "podcast",
            order = 8,
            content = emptyList()
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals(0, result.content.size)
        assertEquals("Empty Content", result.name)
        assertEquals(SectionType.SQUARE, result.type)
        assertEquals(ContentType.PODCAST, result.contentType)
        assertEquals(8, result.order)
    }

    @Test
    fun `toSectionEntity should map two_lines_grid type correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Two Lines Grid",
            type = "2_lines_grid",
            contentType = "podcast",
            order = 9,
            podcastId = "podcast-456"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Two Lines Grid", result.name)
        assertEquals(SectionType.TWO_LINES_GRID, result.type)
        assertEquals(ContentType.PODCAST, result.contentType)
        assertEquals(9, result.order)
    }

    @Test
    fun `toSectionEntity should map big square type correctly`() {
        val sectionResponse = createSectionResponse(
            name = "Big Square Section",
            type = "big square",
            contentType = "podcast",
            order = 10,
            podcastId = "podcast-789"
        )

        val result = sectionResponse.toSectionEntity()

        assertEquals("Big Square Section", result.name)
        assertEquals(SectionType.BIG_SQUARE, result.type)
        assertEquals(ContentType.PODCAST, result.contentType)
        assertEquals(10, result.order)
    }

    // Helper method to create content responses
    private fun createContentResponse(
        name: String = "Podcast Title",
        podcastId: String? = null,
        episodeId: String? = null,
        audiobookId: String? = null,
        articleId: String? = null
    ): SectionContentResponse {
        return SectionContentResponse(
            name = name,
            description = "Podcast Description",
            avatarUrl = "https://example.com/avatar.jpg",
            duration = 3600,
            language = "en",
            score = 4.5,
            podcastId = podcastId,
            episodeCount = 10,
            priority = 1,
            popularityScore = 95,
            episodeId = episodeId,
            podcastPopularityScore = 90,
            podcastPriority = 2,
            seasonNumber = 1,
            episodeType = "full",
            podcastName = "Podcast Name",
            authorName = "Author Name",
            number = 5,
            separatedAudioUrl = "https://example.com/audio-separated.mp3",
            audioUrl = "https://example.com/audio.mp3",
            releaseDate = "2023-01-01",
            chapters = listOf(Chapter(id = "chapter-1")),
            audiobookId = audiobookId,
            articleId = articleId
        )
    }

    // Helper method to create section responses
    private fun createSectionResponse(
        name: String,
        type: String,
        contentType: String,
        order: Int,
        podcastId: String? = null,
        episodeId: String? = null,
        audiobookId: String? = null,
        articleId: String? = null
    ): SectionResponse {
        val contentResponse = createContentResponse(
            podcastId = podcastId,
            episodeId = episodeId,
            audiobookId = audiobookId,
            articleId = articleId
        )

        return SectionResponse(
            name = name,
            type = type,
            contentType = contentType,
            order = order,
            content = listOf(contentResponse)
        )
    }
}