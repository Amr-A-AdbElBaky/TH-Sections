package com.example.thmanyah.features.sections.presentation.uimodel.uimapper

import com.example.thmanyah.features.sections.domain.entity.SectionContentEntity
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiState
import com.example.thmanyah.features.sections.presentation.uimodel.SectionUiState

fun SectionEntity.toSectionUiState() = SectionUiState(
    name = name,
    type = type,
    contentType = contentType,
    order = order,
    content = content.map { it.toSectionContentUiState() }
)

fun SectionContentEntity.toSectionContentUiState() = SectionContentUiState(
    id = id,
    name = name,
    description = description,
    avatarUrl = avatarUrl,
    episodeCount = episodeCount,
    duration = duration,
    language = language,
    priority = priority,
    popularityScore = popularityScore,
    score = score,
    authorName = authorName,
    releaseDate = releaseDate,
)