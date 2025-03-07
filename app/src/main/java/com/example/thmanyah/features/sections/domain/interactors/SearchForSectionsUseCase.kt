package com.example.thmanyah.features.sections.domain.interactors

import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchForSectionsUseCase @Inject constructor(
    private val repository: SectionsRepository
) {
     operator fun invoke(searchQuery: String): Flow<List<SectionEntity>> {
        return repository.searchForSections(searchQuery)
    }
}
