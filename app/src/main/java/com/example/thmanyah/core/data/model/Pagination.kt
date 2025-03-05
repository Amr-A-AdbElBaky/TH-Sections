package com.example.thmanyah.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationModel(
    @SerialName("next_page")
    val nextPage: String?,
    @SerialName("total_pages")
    val totalPages: Int
)