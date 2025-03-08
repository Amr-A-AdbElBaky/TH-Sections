package com.example.thmanyah.features.sections.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.thmanyah.base.presentation.viewmodel.BaseViewModel
import com.example.thmanyah.core.extensions.getMappedMessage
import com.example.thmanyah.features.sections.domain.interactors.GetHomeSectionsUseCase
import com.example.thmanyah.features.sections.domain.interactors.HasSectionsNextPageUseCase
import com.example.thmanyah.features.sections.domain.interactors.SearchForSectionsUseCase
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsEvents
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsIntent
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsListUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsListUiState
import com.example.thmanyah.features.sections.presentation.uimodel.uimapper.toSectionUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.uimapper.toSectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionsViewModel @Inject constructor(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase,
    private val searchForSectionsUseCase: SearchForSectionsUseCase,
    private val hasNextPageUseCase: HasSectionsNextPageUseCase,
    @ApplicationContext private val context: Context,
) : BaseViewModel<SectionsIntent, SectionsListUiState, SectionsListUiModel, SectionsEvents>(
    SectionsListUiState()
) {
    private val _searchQuery = MutableStateFlow("")
    private var currentSearchJob: Job? = null
    init {
        processIntent(SectionsIntent.ListenForNextPage)
        observeSearchQuery()
    }

    override suspend fun handleIntent(intent: SectionsIntent) {
        when (intent) {
            is SectionsIntent.ListenForNextPage -> listenForNextPage()
            is SectionsIntent.LoadHomeSections -> loadHomeSections(true)
            is SectionsIntent.LoadMoreSections -> loadHomeSections(false)
            is SectionsIntent.RefreshHomeSections -> refreshHomeSections()
            is SectionsIntent.SearchByName -> { _searchQuery.value = intent.name }
            is SectionsIntent.SelectCategory -> selectCategory(intent.index)
            is SectionsIntent.SectionItemClicked -> emitEvent(
                SectionsEvents.NavigateToDetails(
                    intent.id,
                    intent.type
                )
            )

            is SectionsIntent.SectionShowMoreClicked -> {

            }
            is SectionsIntent.ClearSearch -> clearSearch()
        }
    }

    private fun selectCategory(index: Int) {
        updateState { it.copy(selectedCategory = state.value.headersCategories[index]) }
    }

    private fun listenForNextPage() {
        hasNextPageUseCase.invoke()
            .onEach { hasNextPage -> updateState { it.copy(hasNextPage = hasNextPage) } }
            .launchIn(viewModelScope)
    }

    private fun loadHomeSections(isFirstPage: Boolean = true) {
        if (isFirstPage || (
                    state.value.hasNextPage
                            && !state.value.isLoading && !state.value.isRefreshing)
        ) {
            getHomeSectionsUseCase.invoke(isFirstPage)
                .onStart {
                    updateState { it.copy(isLoading = true, error = null) }
                }
                .catch { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error =  exception.getMappedMessage(),
                        )
                    }
                }
                .onEach { list ->
                    val newList =
                        if (isFirstPage) list.map { it.toSectionUiState() } else state.value.sections + list.map { it.toSectionUiState() }
                    updateState { state ->
                        state.copy(
                            isRefreshing = false,
                            isLoading = false,
                            sections = newList
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun refreshHomeSections() {
        getHomeSectionsUseCase.invoke(true)
            .onStart {
                updateState { it.copy(isRefreshing = true, error = null) }
            }
            .catch { exception ->
                updateState { it.copy(isRefreshing = false, error = exception.getMappedMessage()) }
            }
            .onEach { list ->
                updateState { state ->
                    state.copy(
                        isRefreshing = false,
                        sections = list.map { it.toSectionUiState() })
                }
            }
            .launchIn(viewModelScope)
    }

    override fun mapStateToUiModel(state: SectionsListUiState): SectionsListUiModel {
        val uiSections = state.sections.map {
            it.toSectionUiModel(context)
        }
        return SectionsListUiModel(
            showFullLoading = state.isLoading && state.sections.isEmpty(),
            showFooterLoading = state.isLoading && state.sections.isNotEmpty(),
            showPullToRefresh = state.isRefreshing,
            showEmptyView = state.sections.isEmpty() && !state.isLoading,
            selectedCategoryIndex = state.headersCategories.indexOf(state.selectedCategory),
            sections = uiSections,
            error = if (state.sections.isEmpty()) state.error else null,
            snackError = if (state.sections.isNotEmpty())state.error else null,
            headersCategories = state.headersCategories
        )
    }


    private fun clearSearch(){
        currentSearchJob?.cancel()
        currentSearchJob = null
        updateState { it.copy(sections = emptyList()) }
    }

    private fun searchSections(query: String) {
        currentSearchJob?.cancel()
        currentSearchJob = searchForSectionsUseCase.invoke(query)
                .onStart {
                    updateState { it.copy(isLoading = true, error = null) }
                }
                .catch { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error= exception.getMappedMessage(),
                        )
                    }
                }
                .onEach { list ->
                    updateState { state ->
                        state.copy(
                            isRefreshing = false,
                            isLoading = false,
                            sections = list.map { it.toSectionUiState() }
                        )
                    }
                }
                .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // 300ms delay
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .collectLatest { query ->
                    searchSections(query)
                }
        }
    }

}