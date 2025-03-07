package com.example.thmanyah.features.sections.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.SectionUiModel
import com.example.thmanyah.ui.theme.colorDarkBlue

@Composable
fun SectionContainerItem(
    section: SectionUiModel,
    onItemClick: (String, ContentType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        SectionHeaderView(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            title = section.title,
            titleColor = section.titleColor
        )

        when (section.type) {
            SectionType.SQUARE -> {
                SquareSection(
                    modifier = Modifier,
                    section = section,
                    onItemClick = onItemClick
                )
            }

            SectionType.TWO_LINES_GRID -> {
                TwoLinesGridSection(
                    modifier = Modifier.fillMaxWidth(), section
                )
            }

            SectionType.BIG_SQUARE -> {
                BigSquareSection(
                    modifier = Modifier,
                    section = section,
                    onItemClick = onItemClick
                )
            }

            SectionType.QUEUE -> {
                QueueSection(Modifier.fillMaxWidth(), section, onItemClick)
            }

            else -> {}
        }
    }
}




@Composable
fun SquareSection(
    modifier: Modifier,
    section: SectionUiModel,
    onItemClick: (String, ContentType) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(section.content) { item ->
            SquareItem(
                modifier = Modifier
                    .width(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        onItemClick(item.id, section.contentType)
                    }), itemContent = item
            )
        }
    }
}

@Composable
fun BigSquareSection(
    modifier: Modifier,
    section: SectionUiModel,
    onItemClick: (String, ContentType) -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp.dp * 0.6f
    val height = width * 0.7f
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(section.content) { item ->
            BigSquareItem(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        onItemClick(item.id, section.contentType)
                    }), itemContent = item
            )
        }
    }
}

@Composable
fun QueueSection(
    modifier: Modifier,
    section: SectionUiModel,
    onItemClick: (String, ContentType) -> Unit
) {
    val itemWidth = LocalConfiguration.current.screenWidthDp.dp * 0.75f
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(section.content){item ->
            QueueItem(
                modifier = Modifier
                    .width(itemWidth)
                    .background(colorDarkBlue, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                    .clickable(onClick = {
                        onItemClick(item.id, section.contentType)
                    }),
                episode = item
            )
        }
    }
}

/**
 * Horizontal pager showing multiple pages, with each page containing
 * two vertical items - exactly as shown in the reference image
 */
@Composable
fun TwoLinesGridSection(
    modifier: Modifier = Modifier,
    section: SectionUiModel,
    onItemClick: (String) -> Unit = {},
    onPlayClick: (String) -> Unit = {},
    onMoreClick: (String) -> Unit = {},
    onPlaylistClick: (String) -> Unit = {},
) {
    val pairedItems by remember {
        derivedStateOf {
            section.content.sortedByDescending { it.priority }
                .chunked(2)
        }
    }
    val pagerState = rememberPagerState(pageCount = { pairedItems.size })
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val pageWidth = screenWidth * 0.8f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 8.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = (screenWidth - pageWidth - 16.dp) // Adjusted to show exactly 10% of next page
            ),
            pageSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            // Each page contains two vertical items
            Column(
                modifier = Modifier
                    .width(pageWidth)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First item
                pairedItems[page].getOrNull(0)?.let { item ->
                    TwoLinesGridItem(
                        item = item,
                        onItemClick = { onItemClick(item.id) },
                        onPlayClick = { onPlayClick(item.id) },
                        onMoreClick = { onMoreClick(item.id) },
                        onPlaylistClick = { onPlaylistClick(item.id) }
                    )
                }

                // Second item (if exists)
                pairedItems[page].getOrNull(1)?.let { item ->
                    TwoLinesGridItem(
                        item = item,
                        onItemClick = { onItemClick(item.id) },
                        onPlayClick = { onPlayClick(item.id) },
                        onMoreClick = { onMoreClick(item.id) },
                        onPlaylistClick = { onPlaylistClick(item.id) }
                    )
                }
            }
        }
    }
}




@Preview
@Composable
fun ShowAllTypes() {
    val sampleItems = listOf(
        SectionContentUiModel(
            id = "1",
            name = "د.فيصل الرفاعي | هل استطاع الإنسان مواكبة التطور؟",
            description = "",
            avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png?s=1400&c=66",
            duration = "30:00",
            priority = 2
        ),
        SectionContentUiModel(
            id = "2",
            name = "The Subscription Trap",
            description = "",
            avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png?s=1400&c=66",
            duration = "30:00",
            priority = 1
        ),
        SectionContentUiModel(
            id = "3",
            name = "مقابلة مع خبير التكنولوجيا",
            description = "",
            avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png?s=1400&c=66",
            duration = "45:00",
            priority = 0
        ),
        SectionContentUiModel(
            id = "4",
            name = "العلم والتكنولوجيا اليوم",
            description = "",
            avatarUrl = "https://media.npr.org/assets/img/2024/01/11/podcast-politics_2023_update1_sq-be7ef464dd058fe663d9e4cfe836fb9309ad0a4d.jpg?s=1400&c=66&f=jpg",
            duration = "25:00",
            priority = 0
        )
    )
    val section = SectionUiModel(
        title = "الحلقات الجديدة",
        content = sampleItems
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 16.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        SquareSection(Modifier.fillMaxWidth(), section.copy(type = SectionType.SQUARE)) { _, _ -> }
        Spacer(modifier = Modifier.height(10.dp))

        BigSquareSection(
            Modifier.fillMaxWidth(), section.copy(
                type = SectionType.BIG_SQUARE,
                contentType = ContentType.PODCAST
            )
        ) { _, _ -> }
        Spacer(modifier = Modifier.height(10.dp))

        QueueSection(
            Modifier.fillMaxWidth(), section.copy(
                type = SectionType.QUEUE,
                contentType = ContentType.EPISODE
            )
        ) { _, _ -> }
        Spacer(modifier = Modifier.height(10.dp))
    }
}