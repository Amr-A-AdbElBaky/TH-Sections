package com.example.thmanyah.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavigationRoute {
    @Serializable
    data object Home : MainNavigationRoute()
    @Serializable
    data object Search : MainNavigationRoute()
    @Serializable
    data object Library : MainNavigationRoute()
    @Serializable
    data object Play : MainNavigationRoute()
    @Serializable
    data object Profile : MainNavigationRoute()
}