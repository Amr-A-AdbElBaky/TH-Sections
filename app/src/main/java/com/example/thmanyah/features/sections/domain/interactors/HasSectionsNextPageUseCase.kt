package com.example.thmanyah.features.sections.domain.interactors

import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HasSectionsNextPageUseCase @Inject constructor(
    private val repository: SectionsRepository
) {
     operator fun invoke(): Flow<Boolean> {
        return repository.hasNextPage()
    }
}
