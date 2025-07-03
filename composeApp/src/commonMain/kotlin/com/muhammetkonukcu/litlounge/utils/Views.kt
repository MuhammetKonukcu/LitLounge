package com.muhammetkonukcu.litlounge.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
expect fun PlatformImage(
    imageURL: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
)