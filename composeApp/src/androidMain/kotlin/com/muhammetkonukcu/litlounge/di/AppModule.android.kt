package com.muhammetkonukcu.litlounge.di

import com.muhammetkonukcu.litlounge.database.getDatabase
import com.muhammetkonukcu.litlounge.room.database.AppDatabase
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<AppDatabase> { getDatabase(get()) }
}