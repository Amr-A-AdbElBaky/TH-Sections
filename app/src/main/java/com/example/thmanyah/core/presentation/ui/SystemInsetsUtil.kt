package com.example.thmanyah.core.presentation.ui

import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A utility object that provides system insets handling across different Android API versions
 */
object SystemInsetsUtil {
    
    /**
     * Returns the appropriate status bar inset padding
     * compatible with all Android API versions
     * 
     * @return The appropriate status bar height
     */
    @Composable
    fun getStatusBarPadding(): Dp {
        val statusBarInsets = WindowInsets.statusBars
            .asPaddingValues()
            .calculateTopPadding()
            
        // Status bar height is generally reported correctly on all API levels
        return statusBarInsets
    }
    
    /**
     * Returns the appropriate navigation bar inset padding
     * with fixes for inconsistent reporting on certain API levels
     * 
     * @return The appropriate navigation bar height
     */
    @Composable
    fun getNavigationBarPadding(): Dp {
        val density = LocalDensity.current
        val navigationBarInsets = WindowInsets.navigationBars
            .asPaddingValues()
            .calculateBottomPadding()
        
        return when {
            // Fix for API 30 (Android 11) excessive inset reporting
            Build.VERSION.SDK_INT == 30 -> {
                // Check if this is likely gesture navigation
                if (navigationBarInsets > 20.dp) {
                    // Use a reasonable fixed height for gesture navigation
                    12.dp
                } else {
                    // For button navigation, use the reported height
                    navigationBarInsets
                }
            }
            
            // For API 29 (Android 10) with possible inconsistencies
            Build.VERSION.SDK_INT == 29 -> {
                minOf(navigationBarInsets, 24.dp)
            }
            
            // For all other API levels, use the system-reported value
            else -> navigationBarInsets
        }
    }
    
    /**
     * Returns complete padding values that can be applied to content
     * to ensure proper spacing from both status bar and navigation bar
     * 
     * @param additionalTop Additional padding to add to the top (beyond status bar)
     * @param additionalBottom Additional padding to add to the bottom (beyond navigation bar)
     * @param horizontalPadding Horizontal padding to apply to both left and right
     * @return PaddingValues that can be directly applied to content
     */
    @Composable
    fun contentPadding(
        additionalTop: Dp = 16.dp,
        additionalBottom: Dp = 16.dp,
        horizontalPadding: Dp = 16.dp
    ): PaddingValues {
        return PaddingValues(
            top = getStatusBarPadding() + additionalTop,
            bottom = getNavigationBarPadding() + additionalBottom,
            start = horizontalPadding,
            end = horizontalPadding
        )
    }
}

/**
 * Use these extension functions for more convenient syntax
 */

/**
 * Returns correct content padding for a screen that should respect both
 * status bar and navigation bar insets with consistent behavior across API levels
 * 
 * @param additionalTop Additional padding to add to the top (beyond status bar)
 * @param additionalBottom Additional padding to add to the bottom (beyond navigation bar)
 * @param horizontalPadding Horizontal padding to apply to both left and right
 * @return PaddingValues that can be directly applied to content
 */
@Composable
fun systemAwarePadding(
    additionalTop: Dp = 0.dp,
    additionalBottom: Dp = 16.dp,
    horizontalPadding: Dp = 0.dp
): PaddingValues {
    return SystemInsetsUtil.contentPadding(
        additionalTop = additionalTop,
        additionalBottom = additionalBottom,
        horizontalPadding = horizontalPadding
    )
}

/**
 * Usage example:
 * 
 * @Composable
 * fun MyScreen() {
 *     // Apply to a LazyColumn's contentPadding
 *     LazyColumn(
 *         contentPadding = systemAwarePadding()
 *     ) {
 *         // Content items
 *     }
 *     
 *     // OR apply to a normal Column with Modifier.padding
 *     Column(
 *         modifier = Modifier.padding(systemAwarePadding())
 *     ) {
 *         // Content
 *     }
 * }
 */