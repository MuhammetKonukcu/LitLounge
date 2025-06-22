package com.muhammetkonukcu.litlounge.model

data class ProfileUiState(
    val name: String = "",
    val dailyPageGoal: Int = 0,
    val monthlyBookGoal: Int = 0,
    val sendNotification: Boolean = false,
)