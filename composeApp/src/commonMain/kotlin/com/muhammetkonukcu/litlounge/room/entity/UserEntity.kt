package com.muhammetkonukcu.litlounge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val dailyPageGoal: Int = 0,
    val monthlyBookGoal: Int = 0,
    val sendNotification: Boolean = false,
)
