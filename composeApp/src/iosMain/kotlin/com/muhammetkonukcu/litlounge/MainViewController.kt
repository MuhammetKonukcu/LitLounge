package com.muhammetkonukcu.litlounge

import androidx.compose.ui.window.ComposeUIViewController
import com.muhammetkonukcu.litlounge.di.initKoin
import com.muhammetkonukcu.litlounge.screen.MainScreen

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    MainScreen()
}