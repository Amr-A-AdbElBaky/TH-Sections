package com.example.thmanyah.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.thmanyah.core.extensions.conditional

@Composable
fun TabsView(
    modifier: Modifier,
    tabs: List<String>?,
    selectedIndex: Int?,
    onTabSelected: (Int) -> Unit
) {
    if (!tabs.isNullOrEmpty()) {
        ScrollableTabRow(
            modifier = modifier,
            selectedTabIndex = selectedIndex ?: 0,
            contentColor = Color.Transparent,
            containerColor = Color.Transparent,
            indicator = {},
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = {
                        onTabSelected(index)
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = {
                        Text(
                            modifier = Modifier
                                .conditional(selectedIndex == index) {
                                    background(Color.Red, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 6.dp)

                                }
                                .padding(vertical = 8.dp),
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedIndex == index) Color.White else Color.Gray,
                            fontWeight = FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

