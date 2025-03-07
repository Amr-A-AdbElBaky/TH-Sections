package com.example.thmanyah.features.sections.presentation.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.thmanyah.core.presentation.ui.DefaultSearchBar
import com.example.thmanyah.core.presentation.ui.StateView
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.presentation.ui.SectionContainerItem
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsEvents
import com.example.thmanyah.features.sections.presentation.uimodel.SectionsIntent
import com.example.thmanyah.features.sections.presentation.viewmodel.SectionsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SectionsSearchScreen(

    onNavigateToDetails: (String, ContentType) -> Unit = { _, _ -> }
) {
    val viewModel: SectionsViewModel = hiltViewModel()
    val homeUiModel by viewModel.uiModel.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SectionsEvents.NavigateToDetails -> {
                    onNavigateToDetails(event.id, event.contentType)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultSearchBar(
                modifier =Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = "", // todo set from ui model
                onQueryChange = {
                    viewModel.processIntent(SectionsIntent.SearchByName(it))
                },
                onClear = {
                    viewModel.processIntent(SectionsIntent.ClearSearch)
                }
                )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                homeUiModel?.sections?.let { sections ->
                    items(sections) { section ->
                        SectionContainerItem(
                            section = section,
                            onItemClick = { itemId, contentType ->
                                viewModel.processIntent(
                                    SectionsIntent.SectionItemClicked(
                                        itemId,
                                        contentType
                                    )
                                )
                            }
                        )
                    }
                }
                item {
                    if (homeUiModel?.showFooterLoading == true) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(40.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 3.dp,
                            )
                        }
                    }
                }
            }
        }

        StateView(Modifier.fillMaxSize(),
            isLoading = homeUiModel?.showFullLoading,
            error = homeUiModel?.error,
            onAction = {})
    }
}









