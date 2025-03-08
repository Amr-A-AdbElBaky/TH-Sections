package com.example.thmanyah.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Composable to safely combine padding for edge-to-edge design
 * that prevents content from being cropped or overlapping with system UI.
 */
@Composable
fun rememberSafeAreaPadding(
    additionalTop: Dp = 0.dp,
    additionalBottom: Dp = 0.dp,
    additionalStart: Dp = 0.dp,
    additionalEnd: Dp = 0.dp,
    respectIme: Boolean = false
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    
    // Get system insets
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val imePadding = if (respectIme) WindowInsets.ime.asPaddingValues() else PaddingValues(0.dp)
    
    return PaddingValues(
        top = statusBarPadding.calculateTopPadding() + additionalTop,
        bottom = maxOf(
            navigationBarPadding.calculateBottomPadding(), 
            if (respectIme) imePadding.calculateBottomPadding() else 0.dp
        ) + additionalBottom,
        start = navigationBarPadding.calculateStartPadding(layoutDirection) + additionalStart,
        end = navigationBarPadding.calculateEndPadding(layoutDirection) + additionalEnd
    )
}

/**
 * A data class to hold safe areas for different parts of the screen
 */
data class SafeAreaInsets(
    val statusBarHeight: Dp,
    val navigationBarHeight: Dp,
    val navigationBarLeft: Dp,
    val navigationBarRight: Dp
) {
    companion object {
        val Zero = SafeAreaInsets(0.dp, 0.dp, 0.dp, 0.dp)
    }
}

/**
 * Local composition provider for safe area insets
 */
val LocalSafeAreaInsets = compositionLocalOf { SafeAreaInsets.Zero }

/**
 * Composable that provides the current safe area insets
 */
@Composable
fun provideSafeAreaInsets(): SafeAreaInsets {
    val layoutDirection = LocalLayoutDirection.current
    
    val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
    val navigationBarInsets = WindowInsets.navigationBars.asPaddingValues()
    
    return SafeAreaInsets(
        statusBarHeight = statusBarInsets.calculateTopPadding(),
        navigationBarHeight = navigationBarInsets.calculateBottomPadding(),
        navigationBarLeft = navigationBarInsets.calculateStartPadding(layoutDirection),
        navigationBarRight = navigationBarInsets.calculateEndPadding(layoutDirection)
    )
}

/**
 * Extension function to correctly combine PaddingValues while respecting safe areas
 */
fun PaddingValues.add(other: PaddingValues, layoutDirection: LayoutDirection): PaddingValues {
    return PaddingValues(
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
        start = this.calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        end = this.calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection)
    )
}

/**
 * Usage example:
 * 
 * @Composable
 * fun MyScreen() {
 *     // Get safe area insets including your custom padding
 *     val padding = rememberSafeAreaPadding(
 *         additionalTop = 16.dp,
 *         additionalStart = 16.dp,
 *         additionalEnd = 16.dp,
 *         additionalBottom = 16.dp
 *     )
 *     
 *     Column(
 *         modifier = Modifier.padding(padding)
 *     ) {
 *         // Your content here will be safely spaced from all system UI elements
 *     }
 * }
 */