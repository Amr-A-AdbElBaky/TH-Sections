package com.example.thmanyah.features.sections.data.model.mapper

import com.example.thmanyah.features.sections.data.model.SectionContentResponse
import com.example.thmanyah.features.sections.data.model.SectionResponse
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionContentEntity
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.entity.SectionType


fun SectionResponse.toSectionEntity(): SectionEntity {
    val contentType = ContentType.fromString(contentType)
    return SectionEntity(
        name = name,
        type = SectionType.fromString(type),
        contentType = contentType,
        order = order,
        content = content.map { it.toSectionContentEntity(contentType) }
    )
}

fun SectionContentResponse.toSectionContentEntity(contentType: ContentType): SectionContentEntity =
    SectionContentEntity(
        id = when (contentType) {
            ContentType.PODCAST -> podcastId ?: ""
            ContentType.EPISODE -> episodeId ?: ""
            ContentType.AUDIO_BOOK -> audiobookId ?: ""
            ContentType.AUDIO_ARTICLE -> articleId ?: ""
            else -> ""
        },
        name = name,
        description = description,
        avatarUrl = avatarUrl,
        episodeCount = episodeCount?:0,
        duration = duration,
        language = language?:"ar",
        priority = priority ?: 0,
        popularityScore = popularityScore ?: 0,
        score = score
        )