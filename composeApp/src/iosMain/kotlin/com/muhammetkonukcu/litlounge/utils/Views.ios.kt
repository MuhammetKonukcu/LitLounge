package com.muhammetkonukcu.litlounge.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.placeholder_dark
import litlounge.composeapp.generated.resources.placeholder_light
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun PlatformImage(
    imageURL: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val isDarkMode = isSystemInDarkTheme()
    val placeholderRes =
        if (isDarkMode) painterResource(Res.drawable.placeholder_dark)
        else painterResource(Res.drawable.placeholder_light)

    AsyncImage(
        model = imageURL,
        modifier = modifier,
        error = placeholderRes,
        contentDescription = null,
        contentScale = contentScale,
        placeholder = placeholderRes
    )
}