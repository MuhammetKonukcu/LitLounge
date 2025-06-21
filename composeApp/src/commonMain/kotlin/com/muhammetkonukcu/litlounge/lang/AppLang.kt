package com.muhammetkonukcu.litlounge.lang

import litlounge.composeapp.generated.resources.Res
import litlounge.composeapp.generated.resources.en
import litlounge.composeapp.generated.resources.tr
import org.jetbrains.compose.resources.StringResource

enum class AppLang(
    val code: String,
    val stringRes: StringResource
) {
    English("en", Res.string.en),
    Turkish("tr", Res.string.tr)
}