package com.example.thmanyah.core.presentation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
        contentColor = colorTransparent,
        windowInsets = WindowInsets(0, 0, 0, 0)
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
                        modifier = Modifier.size(30.dp),
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (index == selectedIndex) MaterialTheme.colorScheme.onBackground else Color.Gray
                    )
                },
                label = null,
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor =  colorTransparent,
                        indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
                    )
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