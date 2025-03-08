package com.example.thmanyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thmanyah.features.sections.presentation.ui.SectionsScreen
import com.example.thmanyah.core.presentation.ui.BottomNavBar
import com.example.thmanyah.core.presentation.ui.BottomNavItem
import com.example.thmanyah.core.presentation.ui.SystemBarsController
import com.example.thmanyah.core.presentation.ui.systemAwarePadding
import com.example.thmanyah.features.sections.presentation.ui.search.SectionsSearchScreen
import com.example.thmanyah.ui.DefaultToolBar
import com.example.thmanyah.ui.navigation.MainNavigationRoute
import com.example.thmanyah.ui.theme.ThmanyahTheme
import com.example.thmanyah.ui.theme.rememberSafeAreaPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainNavController = rememberNavController()

            SystemBarsController(darkTheme = isSystemInDarkTheme())
            ThmanyahTheme {
                //         CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                            .padding(systemAwarePadding()),
                    //    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                        topBar = {
                            DefaultToolBar()
                        },
                        bottomBar = {
                            HomeBottomNavigation(modifier = Modifier.fillMaxWidth(),
                                onNavigate = { route ->
                                    mainNavController.navigate(route)
                                })
                        }
                    ) { paddingValues ->
                        Box(Modifier.padding(paddingValues)) {
                            NavHost(
                                modifier = Modifier.fillMaxSize(),
                                navController = mainNavController,
                                startDestination = MainNavigationRoute.Home
                            ) {
                                composable<MainNavigationRoute.Home> {
                                    SectionsScreen()
                                }
                                composable<MainNavigationRoute.Search> {
                                    SectionsSearchScreen()
                                }
                                composable<MainNavigationRoute.Library> {
                                    Text(text = "Library")
                                }
                                composable<MainNavigationRoute.Play> {
                                    Text(text = "Play")
                                }
                                composable<MainNavigationRoute.Profile> {
                                    Text(text = "Profile")
                                }
                            }
                        }

                    }
                }
            }
            //        }
        }
    }
}

@Composable
fun HomeBottomNavigation(
    modifier: Modifier,
    onNavigate: (MainNavigationRoute) -> Unit,
) {
    val items by remember {
        mutableStateOf(
            listOf(
                BottomNavItem(
                    title = "Home", icon = Icons.Default.Home,
                    navigationRoute = MainNavigationRoute.Home
                ),
                BottomNavItem(
                    title = "Search", icon = Icons.Default.Search,
                    navigationRoute = MainNavigationRoute.Search
                ),
                BottomNavItem(
                    title = "Library", icon = Icons.Default.List,
                    navigationRoute = MainNavigationRoute.Library
                ),
                BottomNavItem(
                    title = "Play", icon = Icons.Default.PlayArrow,
                    navigationRoute = MainNavigationRoute.Play
                ),
                BottomNavItem(
                    title = "Profile", icon = Icons.Default.Person,
                    navigationRoute = MainNavigationRoute.Profile
                )
            )
        )
    }

    BottomNavBar(modifier = modifier, items = items, onNavigate = onNavigate)
}
