package com.muhammetkonukcu.litlounge.utils

interface ImageUrlValidator {
    suspend fun validateUrl(url: String): ImageUrlStatus
    fun isValidUrlFormat(url: String): Boolean
}