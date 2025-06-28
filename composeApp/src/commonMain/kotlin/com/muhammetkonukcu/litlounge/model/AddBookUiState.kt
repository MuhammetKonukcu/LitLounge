package com.muhammetkonukcu.litlounge.model

data class AddBookUiState(
    val name: String = "",
    val totalPage: Int = 0,
    val currentPage: Int = 0,
    val imageURL: String = "",
    val startTimestamp: Long = 0,
    val finishTimestamp: Long = 0,
    val finished: Boolean = false,
)