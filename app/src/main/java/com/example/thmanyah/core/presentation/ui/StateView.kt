package com.example.thmanyah.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.thmanyah.R

@Composable
fun StateView(
    modifier: Modifier = Modifier.fillMaxSize(),
    isLoading: Boolean? = false,
    error: String? = null,
    snackError: String? = null,
    onAction: () -> Unit = {}
) {
    Box(
        modifier = modifier

    ) {
        if (isLoading == true) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(40.dp),
                strokeWidth = 3.dp,
            )
        }
        error?.let {
            Column(Modifier.align(Alignment.Center)) {
                Text(it)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = onAction) {
                    Text(stringResource(R.string.label_retry))

                }
            }
        }
        snackError?.let {
            DefaultSnackBar(
                modifier =
                Modifier.align(Alignment.BottomCenter),
                msg = it
            )
        }
    }
}
