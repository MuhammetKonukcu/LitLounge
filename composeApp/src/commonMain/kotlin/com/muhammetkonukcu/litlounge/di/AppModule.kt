package com.muhammetkonukcu.litlounge.di

import com.muhammetkonukcu.litlounge.viewmodel.HistoryViewModel
import com.muhammetkonukcu.litlounge.viewmodel.HomeViewModel
import com.muhammetkonukcu.litlounge.viewmodel.ProfileViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private fun appModule(): Module = module {
    single<HomeViewModel> { HomeViewModel() }
    single<HistoryViewModel> { HistoryViewModel() }
    single<ProfileViewModel> { ProfileViewModel() }
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)

        modules(
            appModule()
        )
    }