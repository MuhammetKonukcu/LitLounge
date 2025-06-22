package com.muhammetkonukcu.litlounge

import android.app.Application
import com.muhammetkonukcu.litlounge.di.initKoin
import org.koin.android.ext.koin.androidContext

class LitLoungeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LitLoungeApp)
        }
    }
}