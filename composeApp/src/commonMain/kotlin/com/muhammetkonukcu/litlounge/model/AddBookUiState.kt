package com.muhammetkonukcu.litlounge.model

import com.muhammetkonukcu.litlounge.utils.ImageUrlStatus

data class AddBookUiState(
    val id: Int = 0,
    val name: String = "",
    val totalPage: Int = 0,
    val currentPage: Int = 0,
    val imageURL: String = "",
    val authorName: String = "",
    val startTimestamp: String = "",
    val finishTimestamp: String = "",
    val finished: Boolean = false,
    val isImageSaving: Boolean = false,
    val isNetworkImage: Boolean = false,
    val imageUrlStatus: ImageUrlStatus = ImageUrlStatus.IDLE,
)