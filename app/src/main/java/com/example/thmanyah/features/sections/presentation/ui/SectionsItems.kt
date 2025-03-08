package com.example.thmanyah.features.sections.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thmanyah.core.presentation.ui.DefaultImage
import com.example.thmanyah.features.sections.presentation.uimodel.SectionContentUiModel
import com.example.thmanyah.ui.theme.GrayText
import com.example.thmanyah.ui.theme.ThemeColors
import com.example.thmanyah.ui.theme.colorBlackSemiTransparent
import com.example.thmanyah.ui.theme.colorBlackSemiTransparent10
import com.example.thmanyah.ui.theme.colorBlackSemiTransparent25
import com.example.thmanyah.ui.theme.colorBlackSemiTransparent50
import com.example.thmanyah.ui.theme.colorDarkBlue
import com.example.thmanyah.ui.theme.colorTransparent

@Composable
fun SquareItem(
    modifier: Modifier,
    itemContent: SectionContentUiModel,
) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box {
                DefaultImage(
                    imageUrl = itemContent.avatarUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                itemContent.popularityScore?.let {
                    LinearProgressIndicator(
                        drawStopIndicator = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        progress = { it.toFloat() },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Text(
            text = itemContent.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            PlayIconWithDuration(durationLabel = itemContent.duration)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = itemContent.episodeCountLabel,
                color = GrayText,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BigSquareItem(
    modifier: Modifier,
    itemContent: SectionContentUiModel,
) {
    Box(modifier = modifier) {
        DefaultImage(
            imageUrl = itemContent.avatarUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorTransparent,
                            colorBlackSemiTransparent10,
                            colorBlackSemiTransparent25,
                            colorBlackSemiTransparent50,
                            colorBlackSemiTransparent,
                        )
                    ),
                )
                .padding(horizontal = 12.dp)
                .padding(bottom = 12.dp, top = 16.dp)

                .align(Alignment.BottomStart)
        ) {
            Text(
                text = itemContent.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = itemContent.episodeCountLabel,
                color = Color.White,
                fontSize = 12.sp,

                )
        }
    }
}

@Composable
fun QueueItem(
    modifier: Modifier,
    episode: SectionContentUiModel,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            episode.authorName?.let {
                Text(
                    text = it,
                    color = GrayText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            episode.releaseDate?.let {
                Text(
                    text = it,
                    color = GrayText,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = episode.description,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ThemeColors.get.tertiary, RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            DefaultImage(
                imageUrl = episode.avatarUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Column(
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = episode.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = episode.duration,
                    color = GrayText,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }


        }
    }
}

@Composable
fun TwoLinesGridItem(
    item: SectionContentUiModel,
    onItemClick: () -> Unit,
    onPlayClick: () -> Unit,
    onMoreClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onItemClick),
    ) {
        DefaultImage(
            imageUrl = item.avatarUrl,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(120.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .padding(horizontal = 8.dp),
        ) {
            item.releaseDate?.let {
                Text(
                    text = it,
                    color = GrayText,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            PlayIconWithDuration(
                modifier =  Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .clickable { onPlayClick() },
                durationLabel = item.duration
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // More options (three dots)
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Playlist button
            IconButton(
                onClick = onPlaylistClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Playlist",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }

        }
    }
}


@Composable
fun PlayIconWithDuration(
    modifier: Modifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
        .padding(horizontal = 6.dp, vertical = 2.dp),
    durationLabel: String
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = durationLabel,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}