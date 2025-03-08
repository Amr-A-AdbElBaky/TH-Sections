package com.example.thmanyah.core.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.thmanyah.ui.theme.DarkBackground
import com.example.thmanyah.ui.theme.LightBackground

@Composable
fun SystemBarsController(
    darkTheme: Boolean,
    statusBarColor: Color = if (darkTheme) DarkBackground else LightBackground,
    navigationBarColor: Color = if (darkTheme) DarkBackground else LightBackground
) {
    val view = LocalView.current
    val window = (view.context as? android.app.Activity)?.window
    // Configure edge-to-edge display
    DisposableEffect(darkTheme) {
        window?.let {
            // Make the system bars transparent
            WindowCompat.setDecorFitsSystemWindows(it, false)

            // Set transparent colors for system bars
            it.statusBarColor = statusBarColor.toArgb()
            it.navigationBarColor = navigationBarColor.toArgb()

            // Update system bar appearance based on theme
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }

        onDispose {}
}
}