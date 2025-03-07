package com.example.thmanyah.features.sections.data.repository

import com.example.thmanyah.core.data.model.PaginationModel
import com.example.thmanyah.features.sections.data.model.mapper.toSectionEntity
import com.example.thmanyah.features.sections.data.source.remote.SectionsApi
import com.example.thmanyah.features.sections.domain.entity.SectionEntity
import com.example.thmanyah.features.sections.domain.repository.SectionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SectionsRepositoryImpl @Inject constructor(
    private val api: SectionsApi,
) : SectionsRepository {

    private val hasNextPageState = MutableStateFlow(true)
    private var mCurrentPage = 0

    override fun getHomeSections(isFirstPage: Boolean): Flow<List<SectionEntity>> = flow {
        updatePage(isFirstPage)
        val response = api.getHomeSections(page = mCurrentPage, name =null)
        setNextPageConfigurations(response.pagination)
        emit(response.sections.map { it.toSectionEntity() })
    }

    override fun searchForSections(searchQuery: String): Flow<List<SectionEntity>> = flow {
        val response = api.getHomeSections(page = 1, name =searchQuery)
        emit(response.sections.map { it.toSectionEntity() })
    }

    override fun hasNextPage(): Flow<Boolean> {
        return hasNextPageState
    }

    private fun updatePage(firstPage: Boolean) {
        if (firstPage)
            mCurrentPage = 1
    }

    private suspend fun setNextPageConfigurations(pagination: PaginationModel) {
        if (pagination.nextPage == null || pagination.totalPages <= mCurrentPage)
            hasNextPageState.emit(false)
        else{
            mCurrentPage = mCurrentPage.plus(1)
            hasNextPageState.emit(true)
        }
    }

}