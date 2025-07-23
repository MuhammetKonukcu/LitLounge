package com.muhammetkonukcu.litlounge.model

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
)