package com.example.thmanyah.core.presentation.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DefaultSnackBar(
    modifier: Modifier,
    msg: String,
    background: Color = SnackbarDefaults.color,
    textColor: Color = MaterialTheme.colorScheme.surface
) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    SnackbarHost(hostState = snackState, modifier) {
        Snackbar(
            snackbarData = it, containerColor = background, contentColor = textColor,
            shape = RoundedCornerShape(10.dp)
        )

    }
    LaunchedEffect(key1 = msg) {
        snackScope.launch { snackState.showSnackbar(msg) }
    }
}