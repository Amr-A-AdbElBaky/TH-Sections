package com.example.thmanyah.features.sections.presentation.uimodel

import com.example.thmanyah.base.presentation.uimodel.BaseEvent
import com.example.thmanyah.features.sections.domain.entity.ContentType

sealed class SectionsEvents : BaseEvent {
    data class NavigateToDetails(val id: String, val contentType: ContentType) : SectionsEvents()
}