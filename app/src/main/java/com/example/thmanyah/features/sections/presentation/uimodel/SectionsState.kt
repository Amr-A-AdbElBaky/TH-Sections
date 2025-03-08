package com.example.thmanyah.features.sections.presentation.uimodel

import com.example.thmanyah.base.presentation.uimodel.BaseState
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType


data class SectionsListUiState(
    val isLoading: Boolean = false,
    val headersCategories :List<String> = listOf("الكتب", "المقالات الصوتية", "البودكاست"), // todo  get from Api if possible
    val sections: List<SectionUiState> = emptyList(),
    val error: String? = null,
    val selectedCategory:String? = headersCategories.first(), // todo should be assigned after fetching from api
    val hasNextPage: Boolean = false,
    val isRefreshing: Boolean = false,
) : BaseState

data class SectionUiState(
    val name: String ="",
    val type: SectionType = SectionType.UNKNOWN,
    val contentType: ContentType = ContentType.UNKNOWN,
    val order: Int =0,
    val content: List<SectionContentUiState> = emptyList()
)

data class SectionContentUiState(
    val id: String ="",
    val name: String ="",
    val description: String ="",
    val avatarUrl: String ="",
    val episodeCount: Int =0,
    val duration: Long =0L,
    val language: String = "",
    val priority: Int = 0,
    val popularityScore: Int = 0,
    val score: Double? = 0.0,
    val authorName: String? = null,
    val releaseDate: String? = null,
)
