package com.muhammetkonukcu.litlounge.di

import com.muhammetkonukcu.litlounge.database.getDatabase
import com.muhammetkonukcu.litlounge.platform.NotificationManager
import com.muhammetkonukcu.litlounge.room.database.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<AppDatabase> { getDatabase(get()) }
}

actual fun platformModule(): Module = module {
    single { NotificationManager(context = get()) }
}