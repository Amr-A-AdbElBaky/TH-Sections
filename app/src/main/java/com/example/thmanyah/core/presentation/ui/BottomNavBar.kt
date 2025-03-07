package com.example.thmanyah.core.presentation.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.thmanyah.ui.navigation.MainNavigationRoute
import com.example.thmanyah.ui.theme.colorTransparent
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Composable
fun BottomNavBar(
    modifier: Modifier,
    items: List<BottomNavItem>,
    onNavigate: (MainNavigationRoute) -> Unit,
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    NavigationBar(
        modifier = modifier,
        containerColor = colorTransparent,
        contentColor = Color.White
    ) {
        items.forEachIndexed { index, (title, icon, route) ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    onNavigate(route)
                    selectedIndex = index
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (index == selectedIndex) Color.White else Color.Gray
                    )
                },
                label = null
            )
        }
    }
}

@Serializable
data class BottomNavItem(
    val title: String,
    @Contextual val icon: ImageVector,
    val navigationRoute: MainNavigationRoute
)