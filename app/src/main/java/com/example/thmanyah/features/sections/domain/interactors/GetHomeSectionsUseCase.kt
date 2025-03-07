package com.example.thmanyah.features.sections.domain.interactors

import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeSectionsUseCase @Inject constructor(
    private val repository: SectionsRepository
) {
     operator fun invoke(isFirstPage: Boolean): Flow<List<SectionEntity>> {
        return repository.getHomeSections(isFirstPage)
    }
}
