package com.muhammetkonukcu.litlounge

import android.app.Application
import com.muhammetkonukcu.litlounge.di.initKoin
import com.muhammetkonukcu.litlounge.platform.AlarmScheduler
import org.koin.android.ext.koin.androidContext

class LitLoungeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LitLoungeApp)
        }

        AlarmScheduler.scheduleDailyAlarm(
            context = this,
            hour = 20,
            minute = 0
        )
    }
}