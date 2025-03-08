package com.example.thmanyah.features.sections.presentation.uimodel

import androidx.annotation.ColorRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.thmanyah.R
import com.example.thmanyah.base.presentation.uimodel.UiModel
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.ui.theme.ThemeColors

data class SectionsListUiModel(
    val sections: List<SectionUiModel> = emptyList(),
    val headersCategories: List<String> = emptyList(),
    val selectedCategoryIndex: Int? = null,
    val error: String? = null,
    val snackError: String? = null,
    val showFullLoading: Boolean = false,
    val showFooterLoading: Boolean = false,
    val showPullToRefresh: Boolean = false,
    val showEmptyView: Boolean = false,
) : UiModel

data class SectionUiModel(
    val title: String = "",
    val subtitle: String? = null,
    val type: SectionType = SectionType.UNKNOWN,
    val contentType: ContentType = ContentType.UNKNOWN,
    val order: Int = 0,
    val titleColor: Color = ThemeColors.get.onBackground,
    val content: List<SectionContentUiModel> = emptyList()
)

data class SectionContentUiModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val avatarUrl: String = "",
    val episodeCountLabel: String = "",
    val duration: String = "",
    val score: Int? = null,
    val priority: Int = 0,
    val popularityScore: Int? = null,
    val authorName: String? = null,
    val releaseDate: String? = null,
)
