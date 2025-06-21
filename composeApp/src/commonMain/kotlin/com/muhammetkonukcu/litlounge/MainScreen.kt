package com.muhammetkonukcu.litlounge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.muhammetkonukcu.litlounge.lang.AppLang
import com.muhammetkonukcu.litlounge.lang.rememberAppLocale
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalAppLocalization = compositionLocalOf {
    AppLang.English
}

@Composable
@Preview
fun MainScreen() {
    val currentLanguage = rememberAppLocale()
    CompositionLocalProvider(LocalAppLocalization provides currentLanguage) {

    }
}