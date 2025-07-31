package com.muhammetkonukcu.litlounge.platform

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.muhammetkonukcu.litlounge.R

actual class NotificationManager(
    private val context: Context
) {
    private val notificationManager get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @SuppressLint("MissingPermission")
    actual fun showNotification(
        title: String,
        description: String
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        createNotificationChannel()

        if (areNotificationEnabled) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        }
    }

    private val areNotificationEnabled get() = NotificationManagerCompat
        .from(context)
        .areNotificationsEnabled()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }.also { notificationManager.createNotificationChannel(it) }
        }
    }


    companion object {
        private const val CHANNEL_DESCRIPTION = "Daily goals"
        private const val CHANNEL_NAME = "Default Channel"
        private const val CHANNEL_ID = "Notification"
        private const val NOTIFICATION_ID = 1
    }
}