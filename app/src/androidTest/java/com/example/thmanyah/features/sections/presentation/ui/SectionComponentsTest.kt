package com.example.thmanyah.features.sections.presentation.ui


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.thmanyah.R
import com.example.thmanyah.features.sections.domain.entity.ContentType
import com.example.thmanyah.features.sections.domain.entity.SectionType
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiModel
import com.example.thmanyah.features.sections.presentation.uimodel.SectionUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SectionComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sectionHeader_displaysCorrectTitle() {
        // Set up
        composeTestRule.setContent {
            SectionHeaderView(
                modifier = Modifier.fillMaxWidth(),
                title = "Test Header",
                titleColor = Color.Yellow
            )
        }

        // Verify header text is displayed
        composeTestRule.onNodeWithText("Test Header").assertIsDisplayed()
    }

    @Test
    fun sectionHeader_withSubtitle_displaysCorrectly() {
        // Set up
        composeTestRule.setContent {
            SectionHeaderView(
                modifier = Modifier.fillMaxWidth(),
                title = "Test Header",
                subtitle = "Test Subtitle",
                titleColor = Color.Yellow
            )
        }

        // Verify header and subtitle are displayed
        composeTestRule.onNodeWithText("Test Header").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Subtitle").assertIsDisplayed()
    }

    @Test
    fun squareSection_displaysAllItems() {
        // Set up
        val section = createMockSection(SectionType.SQUARE, 3)
        var clickedId = ""
        var clickedType: ContentType? = null

        composeTestRule.setContent {
            SquareSection(
                modifier = Modifier.fillMaxWidth(),
                section = section,
                onItemClick = { id, type ->
                    clickedId = id
                    clickedType = type
                }
            )
        }

        // Verify all items are displayed
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()

        // Click on the first item
        composeTestRule.onNodeWithText("Item 1").performClick()

        // Verify click handler was called with correct data
        assert(clickedId == "item1")
        assert(clickedType == ContentType.PODCAST)
    }

    @Test
    fun queueSection_displaysItemsCorrectly() {
        // Set up
        val section = createMockSection(
            SectionType.QUEUE,
            2,
            ContentType.EPISODE,
            withReleaseDate = true
        )

        composeTestRule.setContent {
            QueueSection(
                modifier = Modifier.fillMaxWidth(),
                section = section,
                onItemClick = { _, _ -> }
            )
        }

        // Verify episode items display correctly
        composeTestRule.onNodeWithText("Item 1").assertExists()
        composeTestRule.onNodeWithText("Description 1").assertExists()
        composeTestRule.onNodeWithText("2023-01-01").assertExists()
    }

    @Test
    fun bigSquareSection_displaysItemsCorrectly() {
        // Set up
        val section = createMockSection(SectionType.BIG_SQUARE, 2)

        composeTestRule.setContent {
            BigSquareSection(
                modifier = Modifier.fillMaxWidth(),
                section = section,
                onItemClick = { _, _ -> }
            )
        }

        // Verify items display correctly
        composeTestRule.onNodeWithText("Item 1").assertExists()
    }

    @Test
    fun sectionContainerItem_displaysHeaderAndContent() {
        // Set up
        val section = createMockSection(SectionType.SQUARE, 2)

        composeTestRule.setContent {
            SectionContainerItem(
                section = section,
                onItemClick = { _, _ -> }
            )
        }

        // Verify header and content are displayed
        composeTestRule.onNodeWithText("Test Section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item 1").assertExists()
    }

    // Helper method to create test data
    private fun createMockSection(
        type: SectionType,
        itemCount: Int,
        contentType: ContentType = ContentType.PODCAST,
        withReleaseDate: Boolean = false
    ): SectionUiModel {
        val content = (1..itemCount).map { index ->
            SectionContentUiModel(
                id = "item$index",
                name = "Item $index",
                description = "Description $index",
                avatarUrl = "https://example.com/image$index.jpg",
                episodeCountLabel = "$index episodes",
                duration = "30:00",
                releaseDate = if (withReleaseDate) "2023-01-0$index" else null,
                authorName = if (withReleaseDate) "Author $index" else null
            )
        }

        return SectionUiModel(
            title = "Test Section",
            type = type,
            contentType = contentType,
            order = 1,
            titleColor = Color.Yellow,
            content = content
        )
    }
}