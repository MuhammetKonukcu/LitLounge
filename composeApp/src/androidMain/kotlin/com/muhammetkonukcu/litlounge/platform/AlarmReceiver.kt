package com.muhammetkonukcu.litlounge.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.muhammetkonukcu.litlounge.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {
        val notificationManager = NotificationManager(ctx)
        val random = (0..2).random()
        val notificationInfoList = listOf(
            ctx.getString(R.string.notification_title_1) to ctx.getString(R.string.notification_text_1),
            ctx.getString(R.string.notification_title_2) to ctx.getString(R.string.notification_text_2),
            ctx.getString(R.string.notification_title_3) to ctx.getString(R.string.notification_text_3)
        )

        notificationManager.showNotification(
            title = notificationInfoList.getOrNull(random)?.first
                ?: notificationInfoList.first().first,
            description = notificationInfoList.getOrNull(random)?.second
                ?: notificationInfoList.first().second
        )
    }
}

