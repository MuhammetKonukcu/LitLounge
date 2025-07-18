package com.muhammetkonukcu.litlounge.di

import com.muhammetkonukcu.litlounge.room.dao.BooksDao
import com.muhammetkonukcu.litlounge.room.dao.PageTrackDao
import com.muhammetkonukcu.litlounge.room.dao.UsersDao
import com.muhammetkonukcu.litlounge.room.database.AppDatabase
import com.muhammetkonukcu.litlounge.room.repository.BooksRepository
import com.muhammetkonukcu.litlounge.room.repository.BooksRepositoryImpl
import com.muhammetkonukcu.litlounge.room.repository.PageTrackRepository
import com.muhammetkonukcu.litlounge.room.repository.PageTrackRepositoryImpl
import com.muhammetkonukcu.litlounge.room.repository.UsersRepository
import com.muhammetkonukcu.litlounge.room.repository.UsersRepositoryImpl
import com.muhammetkonukcu.litlounge.viewmodel.AddBookViewModel
import com.muhammetkonukcu.litlounge.viewmodel.BookDetailViewModel
import com.muhammetkonukcu.litlounge.viewmodel.HistoryViewModel
import com.muhammetkonukcu.litlounge.viewmodel.HomeViewModel
import com.muhammetkonukcu.litlounge.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private fun appModule(): Module = module {
    single<HomeViewModel> { HomeViewModel(get(), get(), get()) }
    single<HistoryViewModel> { HistoryViewModel(get()) }
    single<ProfileViewModel> { ProfileViewModel(get()) }
    viewModel{ AddBookViewModel(get()) }
    viewModel { BookDetailViewModel(get(), get()) }
}

private fun repoModule(): Module = module {
    single<UsersDao> { get<AppDatabase>().getUsersDao() }
    single<UsersRepository> { UsersRepositoryImpl(get()) }

    single<BooksDao> { get<AppDatabase>().getBooksDao() }
    single<BooksRepository> { BooksRepositoryImpl(get()) }

    single<PageTrackDao> { get<AppDatabase>().getPageTrackDao() }
    single<PageTrackRepository> { PageTrackRepositoryImpl(get()) }
}

expect fun databaseModule(): Module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)

        modules(
            appModule(),
            repoModule(),
            databaseModule()
        )
    }