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
import com.muhammetkonukcu.litlounge.utils.DefaultImageUrlValidator
import com.muhammetkonukcu.litlounge.utils.ImageUrlValidator
import com.muhammetkonukcu.litlounge.utils.KtorNetworkClient
import com.muhammetkonukcu.litlounge.utils.NetworkClient
import com.muhammetkonukcu.litlounge.utils.ValidateImageUrlUseCase
import com.muhammetkonukcu.litlounge.viewmodel.AddBookViewModel
import com.muhammetkonukcu.litlounge.viewmodel.BookDetailViewModel
import com.muhammetkonukcu.litlounge.viewmodel.HistoryViewModel
import com.muhammetkonukcu.litlounge.viewmodel.HomeViewModel
import com.muhammetkonukcu.litlounge.viewmodel.ProfileViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private fun appModule(): Module = module {
    single<HomeViewModel> { HomeViewModel(get(), get(), get()) }
    single<HistoryViewModel> { HistoryViewModel(get()) }
    single<ProfileViewModel> { ProfileViewModel(get()) }
    viewModel { AddBookViewModel(get(), get()) }
    viewModel { BookDetailViewModel(get(), get()) }
}

private fun networkModule(): Module = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5_000
                connectTimeoutMillis = 3_000
            }
            install(DefaultRequest) {
                header("User-Agent", "LitLounge/1.0")
            }
        }
    }

    single<NetworkClient> {
        KtorNetworkClient(get())
    }
}

private fun validationModule(): Module = module {
    single<ImageUrlValidator> {
        DefaultImageUrlValidator(get())
    }

    factory<ValidateImageUrlUseCase> {
        ValidateImageUrlUseCase(get())
    }
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

expect fun platformModule(): Module

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)

        modules(
            appModule(),
            repoModule(),
            networkModule(),
            databaseModule(),
            platformModule(),
            validationModule()
        )
    }