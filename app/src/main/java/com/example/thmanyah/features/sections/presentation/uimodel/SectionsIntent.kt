package com.example.thmanyah.features.sections.presentation.uimodel

import com.example.thmanyah.base.presentation.uimodel.BaseIntent
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType


sealed class SectionsIntent : BaseIntent {
    data object LoadHomeSections : SectionsIntent()
    data object RefreshHomeSections : SectionsIntent()
    data object LoadMoreSections: SectionsIntent()
    data object ListenForNextPage: SectionsIntent()
    data object ClearSearch: SectionsIntent()
    data class SearchByName(val name: String) : SectionsIntent()
    data class SelectCategory(val index: Int) : SectionsIntent()
    data class SectionShowMoreClicked(val sectionType: SectionType) : SectionsIntent()
    data class SectionItemClicked(val id: String, val type: ContentType) : SectionsIntent()
}


