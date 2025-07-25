package com.muhammetkonukcu.litlounge.utils

class ValidateImageUrlUseCase(
    private val imageUrlValidator: ImageUrlValidator
) {
    suspend operator fun invoke(url: String): ImageUrlStatus {
        return when {
            url.isBlank() -> ImageUrlStatus.IDLE
            !imageUrlValidator.isValidUrlFormat(url) -> ImageUrlStatus.INVALID
            else -> {
                try {
                    imageUrlValidator.validateUrl(url)
                } catch (e: Exception) {
                    ImageUrlStatus.INVALID
                }
            }
        }
    }
}