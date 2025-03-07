package com.example.thmanyah.features.sections.presentation.uimodel.uimapper

import android.content.Context
import com.example.thmanyah.R
import com.example.thmanyah.core.extensions.formatDurationWitQualifiers
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiState
import com.example.thmanyah.features.sections.presentation.uimodel.SectionUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.SectionUiState

fun SectionUiState.toSectionUiModel(context: Context) = SectionUiModel(
    title = name,
    type = type,
    contentType = contentType,
    order = order,
    titleColor = if(type == SectionType.SQUARE) R.color.yellow else R.color.white,
    content = content.map { it.toSectionContentUiModel(context) }
)

fun SectionContentUiState.toSectionContentUiModel(context: Context) = SectionContentUiModel(
    id = id,
    name = name,
    description = description,
    avatarUrl = avatarUrl,
    episodeCountLabel = "$episodeCount " + context.getString(R.string.label_episodes),
    duration = duration.formatDurationWitQualifiers(context),
    priority = priority,
    popularityScore = popularityScore.div(10),
    score = score?.toInt()?.div(1000),
    authorName = authorName,
    releaseDate = releaseDate
)