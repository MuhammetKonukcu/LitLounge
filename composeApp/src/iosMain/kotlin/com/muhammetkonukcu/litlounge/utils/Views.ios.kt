package com.muhammetkonukcu.litlounge.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
actual fun PlatformImage(
    imageURL: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    AsyncImage(
        model = imageURL,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}