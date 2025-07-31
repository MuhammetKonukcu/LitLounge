package com.muhammetkonukcu.litlounge.platform

expect class NotificationManager {
    fun showNotification(
        title: String,
        description: String
    )
}