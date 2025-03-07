package com.example.thmanyah.core.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.rememberAsyncImagePainter

@Composable
fun DefaultImage(modifier: Modifier,
              imageUrl: String,
              placeholder: Int? = null,
              contentScale: ContentScale = ContentScale.FillBounds){
    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            placeholder = placeholder?.let { painterResource(id = it) }
        ),
        contentDescription = imageUrl,
        contentScale = contentScale,
        modifier = modifier
    )
}