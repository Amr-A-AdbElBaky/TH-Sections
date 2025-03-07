package com.example.thmanyah.features.sections.domain.entity


data class SectionEntity(
    val name: String,
    val type: SectionType,
    val contentType: ContentType,
    val order: Int,
    val content: List<SectionContentEntity>
)

data class SectionContentEntity(
    val id: String,
    val name: String,
    val description: String,
    val avatarUrl: String,
    val episodeCount: Int,
    val duration: Long,
    val language: String,
    val priority: Int,
    val popularityScore: Int,
    val score: Double?,
    val authorName: String? = null,
    val releaseDate: String? = null,
)

enum class SectionType {
    SQUARE,
    BIG_SQUARE,
    TWO_LINES_GRID,
    QUEUE,
    UNKNOWN;

    companion object {
        fun fromString(type: String): SectionType {
            return when (type.lowercase()) {
                "square" -> SQUARE
                "big_square", "big square" -> BIG_SQUARE
                "2_lines_grid" -> TWO_LINES_GRID
                "queue" -> QUEUE
                else -> UNKNOWN
            }
        }
    }
}

enum class ContentType {
    PODCAST,
    EPISODE,
    AUDIO_BOOK,
    AUDIO_ARTICLE,
    UNKNOWN;

    companion object {
        fun fromString(type: String): ContentType {
            return when (type.lowercase()) {
                "podcast" -> PODCAST
                "episode" -> EPISODE
                "audio_book" -> AUDIO_BOOK
                "audio_article" -> AUDIO_ARTICLE
                else -> UNKNOWN
            }
        }
    }
}

