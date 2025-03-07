package com.example.thmanyah.features.sections.domain.repository

import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import kotlinx.coroutines.flow.Flow

interface SectionsRepository {
     fun getHomeSections(isFirstPage: Boolean): Flow<List<SectionEntity>>
     fun searchForSections(searchQuery: String): Flow<List<SectionEntity>>
     fun hasNextPage(): Flow<Boolean>
}