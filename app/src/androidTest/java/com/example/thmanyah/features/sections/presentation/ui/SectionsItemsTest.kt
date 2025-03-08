package com.example.thmanyah.features.sections.presentation.ui


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SectionsItemsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun squareItem_displaysAllElements() {
        // Given
        val item = createTestItem(includePopularityScore = true)

        // When
        composeTestRule.setContent {
            SquareItem(
                modifier = Modifier
                    .width(150.dp)
                    .testTag("square_item"),
                itemContent = item
            )
        }

        // Then
        composeTestRule.onNodeWithTag("square_item").assertExists()
        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.duration).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.episodeCountLabel).assertIsDisplayed()
    }

    @Test
    fun bigSquareItem_displaysCorrectly() {
        // Given
        val item = createTestItem()

        // When
        composeTestRule.setContent {
            BigSquareItem(
                modifier = Modifier
                    .width(200.dp)
                    .height(150.dp)
                    .testTag("big_square_item"),
                itemContent = item
            )
        }

        // Then
        composeTestRule.onNodeWithTag("big_square_item").assertExists()
        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.episodeCountLabel).assertIsDisplayed()
    }

    @Test
    fun queueItem_displaysAllElements() {
        // Given
        val item = createTestItem(includeAuthor = true, includeReleaseDate = true)

        // When
        composeTestRule.setContent {
            QueueItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("queue_item"),
                episode = item
            )
        }

        // Then
        composeTestRule.onNodeWithTag("queue_item").assertExists()
        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.authorName!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.releaseDate!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.duration).assertIsDisplayed()
    }

    @Test
    fun twoLinesGridItem_displaysAllElements() {
        // Given
        val item = createTestItem(includeReleaseDate = true)
        var playClickCount = 0
        var moreClickCount = 0
        var playlistClickCount = 0
        var itemClickCount = 0

        // When
        composeTestRule.setContent {
            TwoLinesGridItem(
                item = item,
                onItemClick = { itemClickCount++ },
                onPlayClick = { playClickCount++ },
                onMoreClick = { moreClickCount++ },
                onPlaylistClick = { playlistClickCount++ }
            )
        }

        // Then
        composeTestRule.onNodeWithText(item.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.releaseDate!!).assertIsDisplayed()
        composeTestRule.onNodeWithText(item.duration).assertIsDisplayed()

        // Test clicks
        composeTestRule.onNodeWithContentDescription("More Options").performClick()
        assert(moreClickCount == 1)

        composeTestRule.onNodeWithContentDescription("Playlist").performClick()
        assert(playlistClickCount == 1)

        // Click on the play icon
        composeTestRule.onNodeWithText(item.duration).performClick()
        assert(playClickCount == 1)

        // Click on the item itself
        composeTestRule.onNodeWithText(item.name).performClick()
        assert(itemClickCount == 1)
    }

    @Test
    fun playIconWithDuration_displaysCorrectly() {
        // Given
        val duration = "45:00"

        // When
        composeTestRule.setContent {
            PlayIconWithDuration(
                modifier = Modifier.testTag("play_icon"),
                durationLabel = duration
            )
        }

        // Then
        composeTestRule.onNodeWithTag("play_icon").assertExists()
        composeTestRule.onNodeWithText(duration).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Play").assertExists()
    }

    // Helper method to create test items
    private fun createTestItem(
        includePopularityScore: Boolean = false,
        includeAuthor: Boolean = false,
        includeReleaseDate: Boolean = false
    ): SectionContentUiModel {
        return SectionContentUiModel(
            id = "test-id",
            name = "Test Item Name",
            description = "This is a test description for the item",
            avatarUrl = "https://example.com/image.jpg",
            episodeCountLabel = "10 episodes",
            duration = "30:00",
            score = 4,
            priority = 1,
            popularityScore = if (includePopularityScore) 85 else null,
            authorName = if (includeAuthor) "Test Author" else null,
            releaseDate = if (includeReleaseDate) "2023-05-15" else null
        )
    }
}