package com.example.thmanyah.features.sections.data.model

import com.example.thmanyah.core.data.model.PaginationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    @SerialName("sections") val sections: List<SectionResponse>,
    @SerialName("pagination") val pagination: PaginationModel
)

@Serializable
data class SectionResponse(
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("content_type") val contentType: String,
    @SerialName("order") val order: Int,
    @SerialName("content") val content: List<SectionContentResponse>
)

@Serializable
data class SectionContentResponse(
    // Common fields
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("duration") val duration: Long,
    @SerialName("language") val language: String? = null,
    @SerialName("score") val score: Double? = null,

    // Podcast fields
    @SerialName("podcast_id") val podcastId: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("priority") val priority: Int? = null,
    @SerialName("popularityScore") val popularityScore: Int? = null,

    // Episode fields
    @SerialName("episode_id") val episodeId: String? = null,
    @SerialName("podcastPopularityScore") val podcastPopularityScore: Int? = null,
    @SerialName("podcastPriority") val podcastPriority: Int? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("episode_type") val episodeType: String? = null,
    @SerialName("podcast_name") val podcastName: String? = null,
    @SerialName("author_name") val authorName: String? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("separated_audio_url") val separatedAudioUrl: String? = null,
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("chapters") val chapters: List<Chapter>? = null,
    @SerialName("paid_is_early_access") val paidIsEarlyAccess: Boolean = false,
    @SerialName("paid_is_now_early_access") val paidIsNowEarlyAccess: Boolean = false,
    @SerialName("paid_is_exclusive") val paidIsExclusive: Boolean = false,
    @SerialName("paid_transcript_url") val paidTranscriptUrl: String? = null,
    @SerialName("free_transcript_url") val freeTranscriptUrl: String? = null,
    @SerialName("paid_is_exclusive_partially") val paidIsExclusivePartially: Boolean = false,
    @SerialName("paid_exclusive_start_time") val paidExclusiveStartTime: Long = 0,
    @SerialName("paid_early_access_date") val paidEarlyAccessDate: String? = null,
    @SerialName("paid_early_access_audio_url") val paidEarlyAccessAudioUrl: String? = null,
    @SerialName("paid_exclusivity_type") val paidExclusivityType: String? = null,

    // Audiobook fields
    @SerialName("audiobook_id") val audiobookId: String? = null,

    // Audio article fields
    @SerialName("article_id") val articleId: String? = null
)

@Serializable
data class Chapter(
    @SerialName("id") val id: String? = null,
)
