package com.muhammetkonukcu.litlounge.utils

import io.ktor.http.isSuccess
import kotlinx.coroutines.withTimeout

class DefaultImageUrlValidator(
    private val networkClient: NetworkClient
) : ImageUrlValidator {

    override fun isValidUrlFormat(url: String): Boolean {
        return try {
            url.startsWith("http://") || url.startsWith("https://")
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun validateUrl(url: String): ImageUrlStatus {
        return try {
            val response = withTimeout(5000) {
                networkClient.head(url)
            }

            if (response.status.isSuccess()) {
                val contentType = response.headers["Content-Type"]
                if (contentType?.startsWith("image/") == true) {
                    ImageUrlStatus.VALID
                } else {
                    ImageUrlStatus.INVALID
                }
            } else {
                ImageUrlStatus.INVALID
            }
        } catch (e: Exception) {
            ImageUrlStatus.INVALID
        }
    }
}