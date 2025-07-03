package com.muhammetkonukcu.litlounge.utils

import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import coil3.compose.AsyncImage

@Composable
actual fun PlatformImage(
    imageURL: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageURL) {
        if (imageURL.isNotEmpty()) {
            try {
                val bitmap = when {
                    imageURL.startsWith("content://") -> {
                        val uri = imageURL.toUri()
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }

                    imageURL.startsWith("file://") -> {
                        BitmapFactory.decodeFile(imageURL.removePrefix("file://"))
                    }

                    else -> {
                        null
                    }
                }
                imageBitmap = bitmap?.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                imageBitmap = null
            }
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else if (!imageURL.startsWith("content://") && !imageURL.startsWith("file://")) {
        AsyncImage(
            model = imageURL,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}